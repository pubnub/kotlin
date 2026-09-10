# DataSync integration-test class provisioning

The DataSync integration tests reference custom **`Test*` classes** by name (`TestNode`,
`TestUser`, `TestFriendship`, `TestOwnership`). There is **no SDK method to create classes** —
the SDK only creates *instances* (entities, relationships, memberships) that reference a class
by name. The classes themselves must exist on the keyset beforehand, provisioned out-of-band via
the **direct class-management REST API** (the admin/metadata plane). These scripts do that.

| Script              | What it does                                                        |
|---------------------|---------------------------------------------------------------------|
| `create-classes.sh` | Registers the four `Test*` classes (POST each version).             |
| `delete-classes.sh` | Removes the four `Test*` classes (DELETE each; 404 = already gone).  |
| `shared.sh`         | Shared helpers (not runnable on its own).                           |

## The four classes

| Class            | Kind         | Cardinality  | Custom properties                                                            |
|------------------|--------------|--------------|------------------------------------------------------------------------------|
| `TestNode` v1    | entity       | —            | none (generic; used for both ends of every relationship)                     |
| `TestUser` v1    | entity       | —            | `username` (full), `email` (simple, **admin-only** projection), `status` (simple), `signupDate` (date, simple) |
| `TestFriendship` v1 | relationship | many-to-many | `status` (full); sides `TestNode`↔`TestNode`                              |
| `TestOwnership` v1  | relationship | one-to-one   | none; sides `TestNode`↔`TestNode` (only exists to make DS-0801 reachable)  |

**Not provisioned:** `User`/`Channel` are built-in **Global** classes (a SubKey class of that
name would shadow the built-in one) and `Membership` is not a class here (it is a MANY_TO_MANY
relationship). The scripts only ever address the four `Test*` names.

## Usage

```bash
# create (idempotent-ish: a 409 means the version already exists — delete first to change a schema)
SDK_DS_META_BASE=<admin-host> DS_ADMIN_VERSION=<yyyy-mm-dd> \
  SDK_DS_API_KEY=<admin-api-key> SDK_DS_SUB_KEY=<sub-key> ./create-classes.sh

# remove (404s are fine)
SDK_DS_META_BASE=<admin-host> DS_ADMIN_VERSION=<yyyy-mm-dd> \
  SDK_DS_API_KEY=<admin-api-key> SDK_DS_SUB_KEY=<sub-key> ./delete-classes.sh
```

Requires `curl` and `jq`. Either script exits non-zero if any request returned an unexpected
status.

### Environment

| Variable            | Required | Meaning                                                                 |
|---------------------|----------|-------------------------------------------------------------------------|
| `SDK_DS_API_KEY`    | yes      | **Admin API key** (admin/console credential). NOT a PAM key/token/secret; sent verbatim as `Authorization`, unsigned. |
| `SDK_DS_SUB_KEY`    | yes      | Subscribe key the classes are registered under.                         |
| `DS_ADMIN_VERSION`  | yes      | `Pubnub-Version` header sent with every request (e.g. `2026-09-03`).    |
| `SDK_DS_META_BASE`  | yes      | Metadata (admin plane) base URL, e.g. `https://<admin-host>`. Local dev typically `http://localhost:8090`. |

`SDK_DS_META_BASE` must point at the admin host of the environment where the integration tests run;
provision the classes on the same keyset the ITs use (`SDK_DS_SUB_KEY`). Keep the host and credentials
out of source — set them via your git-ignored `test.properties` or your shell environment.

## Eventual-consistency note for `full` / `filter` tests

Properties declared `filtering: "full"` (here `TestUser.username` and `TestFriendship.status`) are
indexed into **OpenSearch**, which backs the SDK's `filter` param and is **eventually consistent**.
There is a write-to-index delay between a successful write and when a record becomes visible to a
`filter` query, so **tests asserting on `filter` must poll/await with a bounded retry** — an
immediate read will flake.

`filtering: "simple"` properties (`email`, `status`, `signupDate`) are Postgres-backed, powering
the strongly-consistent `filterFast` (+ `sort`) param — those reads are immediate.
