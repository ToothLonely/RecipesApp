# Приложение рецептов

Это приложение рецептов разных блюд, в котором рецепты разделены по категориям.  
Приложение написано в соответствии с рекомендациями Google, по принципам Clean Architecture

### Автор

**ToothLonely**

# Стек:

| Категория     | Технология                                                         |
|---------------|--------------------------------------------------------------------|
| Architecture  | ![MVVM](https://img.shields.io/badge/MVVM-purple)                  |
| Language      | ![Kotlin](https://img.shields.io/badge/Kotlin-blue)                |
| UI            | ![Views](https://img.shields.io/badge/Views-XML-lightgrey)         |
| Network       | ![Retrofit](https://img.shields.io/badge/Retrofit-green)           |
| Database      | ![Room](https://img.shields.io/badge/Room-red)                     |
| DI            | ![Hilt](https://img.shields.io/badge/Hilt-orange)                  |
| Image Loading | ![Glide](https://img.shields.io/badge/Glide-yellow)                |
| Concurrency   | ![Coroutines](https://img.shields.io/badge/Coroutines-yellowgreen) |

# Структура проекта:

| Package | Content                                                     |
|---------|-------------------------------------------------------------|
| app/    | Application                                                 | 
| data/   | Database, API service, DAOs, DI Module, repository          | 
| model/  | Модели данных (data classes) для использования в приложении | 
| ui/     | Fragments, ViewModels, Adapters, MainActivity               |

### Используемые технологии