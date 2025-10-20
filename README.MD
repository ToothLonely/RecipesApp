# Приложение рецептов

Это приложение рецептов разных блюд, в котором рецепты разделены по категориям.  
Приложение написано в соответствии с рекомендациями Google, по принципам Clean Architecture

### Автор

**ToothLonely**

# Стек:

| Категория     | Технология        |
|---------------|-------------------|
| Architecture  | MVVM              |
| UI            | Views + XML       |
| Network       | Retrofit          |
| Database      | Room              |
| DI            | Hilt              |
| Image Loading | Glide             |
| Concurrency   | Kotlin Coroutines |

# Структура проекта:

| Package | Content                                                     |
|---------|-------------------------------------------------------------|
| app/    | Application                                                 | 
| data/   | Database, API service, DAOs, DI Module, repository          | 
| model/  | Модели данных (data classes) для использования в приложении | 
| ui/     | Fragments, ViewModels, Adapters, MainActivity               |