set -e
echo "Build JS Chat SDK module artifacts"
./gradlew :jsNodeProductionLibraryDistribution #buduje JS lokalnie
./gradlew :packJsPackage #to jest potrzebne bo index.dts jest includowane
mkdir -p js-chat/dist
cp build/packages/js/package.json pubnub-matchmaking-kotlin/js-matchmaking/package.json
cp build/packages/js/index.d.ts pubnub-matchmaking-kotlin/js-matchmaking/dist/                 #todo zmienic nazwe pliku index.d.ts
cp build/packages/js/index.d.ts pubnub-matchmaking-kotlin/js-matchmaking/dist/index.es.d.ts    #todo zmienic nazwe pliku index.d.ts
