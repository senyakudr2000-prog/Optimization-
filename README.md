# FastChunks (Fabric, 1.21.11, client)

Что делает:
- Растягивает обработку пакетов чанков / света / выгрузки по тикам (главное против лагов при ходьбе на 16 чанках).
- Лимит новых частиц за тик.
- Не рисует далёкие сущности (кроме игроков).
- Конфиг: config/fastchunks.properties, профили low / medium / high / custom.

Сборка (нужны JDK 21 и Gradle 8.x):
    gradle build
Готовый jar: build/libs/fastchunks-0.1.0.jar -> положить в mods.

Если сборка ругается на версии: открой https://fabricmc.net/develop,
выбери 1.21.11 и подставь yarn_mappings / loader_version / loom_version в gradle.properties.
Если ругается на имя класса/метода в Mixin — пришли ошибку, поправлю.

Настройки в игре: Настройки (Options) -> кнопка FastChunks в левом верхнем углу.
