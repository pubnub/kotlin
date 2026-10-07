#!/usr/bin/env bash
#
# create-classes.sh — register the custom Test* DataSync classes the integration
# test suites expect to find on the keyset.
#
# There is no SDK method to create classes — the SDK only creates *instances*
# (entities, relationships, memberships) that reference a class by name. The classes
# themselves must exist on the keyset beforehand; this script provisions them.
#
# A 409 on POST means the class version already exists — run delete-classes.sh first
# if you need to change a schema, then re-run this.
#
# NEVER touches User/Channel (built-in Global classes) or Membership (not a class here). It only
# registers SubKey subclasses that extend them (TestSubUser, TestSubChannel).
#
# Usage:
#   SDK_DS_API_KEY=... SDK_DS_SUB_KEY=... ./create-classes.sh

set -euo pipefail

. "$(dirname "${BASH_SOURCE[0]}")/shared.sh"

require_environment

# How long an entity/relationship of these classes lives before the server expires it.
TTL_SEC=259200 # 3 days

# TestNode — generic entity used for BOTH sides of every relationship. No custom props.
say "entity-class: TestNode v1 (generic node, no custom props)"
meta_post "$META/entity-classes/TestNode/versions/1" "$ENTITY_CLASS_MT" "$(cat <<JSON
{
  "data": {
    "description": "IT generic node entity class",
    "config": { "ttlSec": $TTL_SEC }
  }
}
JSON
)"

# TestUser — username=full (covers filter + filterFast + sort), email/status/signupDate=simple.
# Projections: email is admin-only (the discriminating field); the rest are in __default__ + admin.
say "entity-class: TestUser v1 (username full; email/status/signupDate simple; email admin-only projection)"
meta_post "$META/entity-classes/TestUser/versions/1" "$ENTITY_CLASS_MT" "$(cat <<JSON
{
  "data": {
    "description": "IT user entity class",
    "config": { "ttlSec": $TTL_SEC },
    "properties": [
      { "name": "username",   "path": "/payload/username",   "valueKind": "string", "filtering": "full",   "isNullable": false, "projections": [{ "name": "__default__" }, { "name": "admin" }] },
      { "name": "email",      "path": "/payload/email",      "valueKind": "string", "filtering": "simple", "isNullable": true,  "projections": [{ "name": "admin" }] },
      { "name": "status",     "path": "/status",             "valueKind": "string", "filtering": "simple", "isNullable": true,  "projections": [{ "name": "__default__" }, { "name": "admin" }] },
      { "name": "signupDate", "path": "/payload/signupDate", "valueKind": "date",   "filtering": "simple", "isNullable": true,  "projections": [{ "name": "__default__" }, { "name": "admin" }] }
    ]
  }
}
JSON
)"

# TestSubUser — SubKey subclass of the built-in Global User v1. Inherits User's `name`/`type` (full) and adds
# `email` (simple), which is only filterable/sortable when getUsers is scoped to className = TestSubUser.
say "entity-class: TestSubUser v1 (extends User v1; email simple)"
meta_post "$META/entity-classes/TestSubUser/versions/1" "$ENTITY_CLASS_MT" "$(cat <<JSON
{
  "data": {
    "description": "IT User subclass",
    "extends": { "name": "User", "version": 1 },
    "config": { "ttlSec": $TTL_SEC },
    "properties": [
      { "name": "email", "path": "/payload/email", "valueKind": "string", "filtering": "simple", "isNullable": true, "projections": [{ "name": "__default__" }] }
    ]
  }
}
JSON
)"

# TestSubChannel — SubKey subclass of the built-in Global Channel v1. Inherits Channel's `name`/`type` (full) and
# adds `topic` (simple), which is only filterable/sortable when getChannels is scoped to className = TestSubChannel.
say "entity-class: TestSubChannel v1 (extends Channel v1; topic simple)"
meta_post "$META/entity-classes/TestSubChannel/versions/1" "$ENTITY_CLASS_MT" "$(cat <<JSON
{
  "data": {
    "description": "IT Channel subclass",
    "extends": { "name": "Channel", "version": 1 },
    "config": { "ttlSec": $TTL_SEC },
    "properties": [
      { "name": "topic", "path": "/payload/topic", "valueKind": "string", "filtering": "simple", "isNullable": true, "projections": [{ "name": "__default__" }] }
    ]
  }
}
JSON
)"

# TestFriendship — MANY_TO_MANY, both sides TestNode. status=full covers relationship filter/sort.
# secret=admin-only projection (the discriminating field) — mirrors TestUser.email; exercises the
# relationship projection read + DS-0202 write-guard.
say "relationship-class: TestFriendship v1 (many-to-many, TestNode<->TestNode, status full; secret admin-only projection)"
meta_post "$META/relationship-classes/TestFriendship/versions/1" "$RELATIONSHIP_CLASS_MT" "$(cat <<JSON
{
  "data": {
    "cardinality": "many-to-many",
    "entityAClass": "TestNode",
    "entityBClass": "TestNode",
    "properties": [
      { "name": "status", "path": "/status",         "valueKind": "string", "filtering": "full",   "isNullable": true },
      { "name": "secret", "path": "/payload/secret", "valueKind": "string", "filtering": "simple", "isNullable": true, "projections": [{ "name": "admin" }] }
    ]
  }
}
JSON
)"

# TestOwnership — ONE_TO_ONE, both sides TestNode. Only exists to make DS-0801 (cardinality 409) reachable.
say "relationship-class: TestOwnership v1 (one-to-one, TestNode<->TestNode)"
meta_post "$META/relationship-classes/TestOwnership/versions/1" "$RELATIONSHIP_CLASS_MT" "$(cat <<JSON
{
  "data": {
    "cardinality": "one-to-one",
    "entityAClass": "TestNode",
    "entityBClass": "TestNode"
  }
}
JSON
)"

finish
