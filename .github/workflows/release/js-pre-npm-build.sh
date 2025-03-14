set -e
echo "Build JS Matchmaking SDK module artifacts"
./gradlew pubnub-matchmaking-kotlin:jsNodeProductionLibraryDistribution #build JS locally, command: ./gradlew pubnub-matchmaking-kotlin:jsNodeProductionLibraryDistribution
./gradlew pubnub-matchmaking-kotlin:packJsPackage #this is configured by npmPublish in pubnub-matchmaking-kotlin/build.gradle.kts
mkdir -p pubnub-matchmaking-kotlin/js-matchmaking/dist
cp pubnub-matchmaking-kotlin/build/packages/js/package.json pubnub-matchmaking-kotlin/js-matchmaking/package.json
cp pubnub-matchmaking-kotlin/build/dist/js/productionLibrary/pubnub-pubnub-matchmaking-kotlin.d.ts pubnub-matchmaking-kotlin/js-matchmaking/dist/                 #todo zmienic nazwe pliku index.d.ts
cp pubnub-matchmaking-kotlin/build/dist/js/productionLibrary/pubnub-pubnub-matchmaking-kotlin.d.ts pubnub-matchmaking-kotlin/js-matchmaking/dist/pubnub-pubnub-matchmaking-kotlin.es.d.ts    #todo zmienic nazwe pliku index.d.ts
