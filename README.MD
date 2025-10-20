# Приложение рецептов

Это приложение рецептов разных блюд, в котором рецепты разделены по категориям.
Приложение написано в соответствии с рекомендациями Google, по принципам Clean Architecture

### Автор

**ToothLonely**

# Стек:

1. **Architecture:** MVVM
2. **UI:** Views + XML
3. **Network:** Retrofit
4. **Database:** Room
5. **Image Loading:** Glide
6. **Concurrency:** Kotlin Coroutines
7. **DI:** Hilt

# Структура проекта:

| Package | Content                                                     |
|---------|-------------------------------------------------------------|
| app/    | Application                                                 | 
| data/   | Database, API service, DAOs, DI Module, repository          | 
| model/  | Модели данных (data classes) для использования в приложении | 
| ui/     | Fragments, ViewModels, Adapters, MainActivity               |