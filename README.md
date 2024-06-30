# Kotlin oktatáshoz házifeladat

## Készíteni kell egy nagyon egyszerű alkalmazást / programot, az alábbi funkcionalitással
1. Open Meteo API-ról Budapestre 7 napos hőmérsékleti előrejelzés lekérése órás bontásban (https://api.open-meteo.com/v1/forecast?latitude=47.4984&longitude=19.0404&hourly=temperature_2m&timezone=auto)
2. Kapott eredményekre napi középhőmérséklet számítás
3. Eredmények konzolra kiírása
4. Egyszerű hibakezelés
5. Automata teszt(ek)

## Szorgalmi feladat
- Nem csak konzolra írni az eredményt, hanem az alkalmazás (egyszerű webszerver) egy html oldalon elérhetővé teszi azt.
- Nem (csak) Spring Boot, hanem Kotlin-os framework-ok használata (Ktor, kotlinx.serialization, stb)

# Megavlósítás

Kotlin multiplatform project ktor + kotlinjs. 

## Alkalmazás futtatása:

```shell
./gradlew runShadow
```

```shell
open http://localhost:8080
```


