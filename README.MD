# Приложение рецептов

Это приложение рецептов разных блюд, в котором рецепты разделены по категориям.  
Приложение написано в соответствии с рекомендациями Google, по принципам Clean Architecture

### Автор

**ToothLonely**

# Стек:

| Категория     | Технология                                                 |
|---------------|------------------------------------------------------------|
| Architecture  | MVVM                                                       |
| Language      | ![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue) |
| UI            | Views + XML                                                |
| Network       | Retrofit                                                   |
| Database      | Room                                                       |
| DI            | ![Hilt](https://img.shields.io/badge/Hilt-orange)          |
| Image Loading | Glide                                                      |
| Concurrency   | Kotlin Coroutines                                          |

# Структура проекта:

| Package | Content                                                     |
|---------|-------------------------------------------------------------|
| app/    | Application                                                 | 
| data/   | Database, API service, DAOs, DI Module, repository          | 
| model/  | Модели данных (data classes) для использования в приложении | 
| ui/     | Fragments, ViewModels, Adapters, MainActivity               |

### Используемые технологии

![Retrofit](https://img.shields.io/badge/Network-Retrofit-green)
![Room](https://img.shields.io/badge/DB-Room-red)
![Glide](https://img.shields.io/badge/Images-Glide-yellow)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-purple)