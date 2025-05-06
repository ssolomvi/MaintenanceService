Смоделируйте систему “сервисный центр”. В СЦ могут обращаться
клиенты с запросами на ремонт техники (вид техники выберите
самостоятельно) (запросы могут поступать не последовательно). При
ремонте может понадобиться замена некоторых компонентов, которые
должны быть запрошены из пула, а при их отсутствии - компоненты
необходимо произвести на базе существующих “чертежей” (примените
паттерны проектирования “абстрактная фабрика”, “одиночка” (для
фабрики) и “прототип”). При замене компонентов, сломанные компоненты
отправляются “в мастерскую” (отдельный сервис) для ремонта (ремонт
занимает некоторое количество времени) и при успешном ремонте
помещаются в пул, а при неуспешном - отправляются на утилизацию.
Механики ремонта, утилизации, поступления новых клиентов определите
самостоятельно. Для модели реализуйте фасад, позволяющий получать:
состояние пула, активные запросы клиентов, состояние мастерской,
информацию об утилизированных компонентах. Время в системе
дискретно. Начальные настройки частей системы должны быть
псевдослучайными. Продемонстрируйте работу системы. Обеспечьте
наглядный вывод информации о процессе работы и результатах работы
системы.

![Схема](img/scheme.png)

```plantuml
@startuml SoulRepairCenter
skinparam style strictuml
skinparam maxMessageSize 150
skinparam monochrome true

actor Клиент
participant MaintenanceController
participant RepairingService
participant SoulComponentFactory
collections ComponentPool
participant WorkshopService
collections WorkshopLog

Клиент->>MaintenanceController: POST /fix/{тип души}
MaintenanceController->>RepairingService: Добавить в очередь запросов на починку
MaintenanceController-->>Клиент: 200 OK
loop Обработка очереди запросов\nна починку души (Scheduled)
    group Проверка исправности компонентов
    alt  Компонент исправен
        RepairingService-->>RepairingService: Пропустить
    else Компонент сломан
        RepairingService->>ComponentPool: Поиск замены
        alt Найден в пуле
            ComponentPool-->>RepairingService: Вернуть компонент
        else Не найден
            RepairingService->>SoulComponentFactory: Создать новый компонент
            SoulComponentFactory-->>RepairingService: Вернуть компонент
        end
        RepairingService->>WorkshopService: Отправить на ремонт
        WorkshopService->>WorkshopLog: Запись о запросе\nпо починке компонента (RepairingStatus.PENDING)
        loop Обработка очереди запросов\nна починку компонентов души (Scheduled)
            WorkshopService->>WorkshopService: Попытка ремонта
            alt  Ремонт успешный
                WorkshopService->>ComponentPool: Добавить в пул
                WorkshopService->>WorkshopLog: Обновить статус запроса\nпо починке компонента (RepairingStatus.SUCCESS)
            else Ремонт безуспешный
                WorkshopService->>WorkshopLog: Обновить статус запроса\nпо починке компонента (RepairingStatus.FAILURE)
            end
        end
    end
end

@enduml
```
![Sequence diagram](img/seqDiagram.png)

```plantuml
@startuml ActivityDiagram
title Процесс ремонта души в сервисном центре
start

:Клиент отправляет душу на ремонт;
if (Тип души?) then (Human)
  :Вызов cureHumanSoul();
else (Artificial)
  :Вызов cureArtificialSoul();
endif

:Добавление в очередь RepairingService;

repeat
  :Извлечение души из очереди;
  
  repeat :Проверка компонентов;
    if (Компонент сломан?) then (Да)
      fork
        :Поиск в ComponentPool;
        if (Найден?) then (Нет)
        :Создание через фабрику;
        endif 
        :Добавление в душу;
      fork again
        :Отправка сломанного в WorkshopService;
        :Ремонт в WorkshopService;
        if (Ремонт успешен?) then (Да)
            :Добавить в ComponentPool;
        else (Нет)
            :Утилизировать;
  endif
      end fork
    else (Нет)
      :Оставить компонент;
    endif
  repeat while (Есть компоненты?) is (Да)
  ->Нет;
  

repeat while (Есть души в очереди?) is (Да)
->Нет;

stop
@enduml
```
![ActivityDiagram](img/activityDiagram.png)

```plantuml
@startuml Model

' Основные классы компонентов

interface Prototype<T> {
  + cloneSelf(): T
}

abstract class RepairablePrototype<T> {
  + UUID id
  + Boolean broken
  + calculateRepairSuccess() : int
  + calculateRepairTime() : long
  + calculateCreationTime() : long
}

abstract class Consciousness<Consciousness>
abstract class EmotionChip<EmotionChip>
abstract class MemoryModule<MemoryModule>

class HumanConsciousness
class HumanEmotionChip
class HumanMemoryModule

class ArtificialConsciousness
class ArtificialEmotionChip
class ArtificialMemoryModule

Consciousness <|-- HumanConsciousness
EmotionChip <|-- HumanEmotionChip
MemoryModule <|-- HumanMemoryModule
Consciousness <|-- ArtificialConsciousness
EmotionChip <|-- ArtificialEmotionChip
MemoryModule <|-- ArtificialMemoryModule
RepairablePrototype <|-- Consciousness
RepairablePrototype <|-- EmotionChip
RepairablePrototype <|-- MemoryModule
Prototype <|.. RepairablePrototype

' Классы душ
abstract class Soul {
  + Consciousness consciousness
  + EmotionChip emotionChip
  + MemoryModule memoryModule
}

class HumanSoul
class ArtificialSoul

Soul <|-- HumanSoul
Soul <|-- ArtificialSoul

@enduml
```
![model](img/model.png)

```plantuml
@startuml
' Фабрики
interface SoulComponentFactory {
    + createConsciousness(): Consciousness
    + createEmotionChip(): EmotionChip
    + createMemoryModule(): MemoryModule
    + createComponentBy(T another): T
}

abstract class AbstractSoulComponentFactory {
    - imitateComponentCreation(T another): T
}

class HumanSoulComponentFactory {
    - HumanConsciousness
    - HumanEmotionChip
    - HumanMemoryModule
}

class ArtificialSoulComponentFactory {
    - ArtificialConsciousness
    - ArtificialEmotionChip
    - ArtificialMemoryModule
}

AbstractSoulComponentFactory <|-- HumanSoulComponentFactory
AbstractSoulComponentFactory <|-- ArtificialSoulComponentFactory
SoulComponentFactory <|.. AbstractSoulComponentFactory

SoulComponentFactory --> Consciousness
SoulComponentFactory --> EmotionChip
SoulComponentFactory --> MemoryModule
@enduml
```
![Фабрики](img/factory.png)

```plantuml
@startuml
' Сервисы
class RepairingService {
  - BlockingQueue<Soul> queue
  + putForRepairing()
  + getActiveQueriesByPageAndCount()
  - repairSoul()
}

class WorkshopService {
  - BlockingQueue<RepairablePrototype> queue
  + putForRepair()
  + getActiveQueriesByPageAndCount()
  - handleComponentRepairing()
}

class ComponentProvider {
    - ArtificialSoulComponentFactory aFactory
    - HumanSoulComponentFactory hFactory
    + getComponentBy()
    - configureFactory()
}

class ComponentPool {
  Map<Class<?>, List<RepairablePrototype>> pools
  + getByPageAndCount()
  + getComponentByClass()
  + put()
}

class WorkshopLog {
    Map<UUID, WorkshopLogEntity> workshopLog
  + getByPageAndSize()
  + getRepairedByPageAndSize()
  + getUtilizedByPageAndSize()
  + put()
  + refreshComponentStatus()
}

class WorkshopLogEntity {
   - RepairablePrototype component
   - LocalDateTime repairingStartDatetime
   - LocalDateTime repairingEndDatetime
   - RepairingStatus repairingStatus
}

' Связи

ComponentProvider --> SoulComponentFactory

RepairingService --> ComponentProvider
RepairingService --> WorkshopService

WorkshopService --> ComponentPool
WorkshopService --> WorkshopLog

WorkshopLog --> WorkshopLogEntity

@enduml
```
![Сервисы](img/service.png)

```plantuml
@startuml

' Контроллеры
class MaintenanceController {
  + cureHumanSoul(HumanSoul soul)
  + cureArtificialSoul(ArtificialSoul soul)
}

class MaintenanceFacadeController {
  + getComponentPoolElements()
  + getActiveQueries()
  + getWorkshopQueries()
  + getWorkshopLog()
  + getRepaired()
  + getUtilized()
}

MaintenanceController --> RepairingService
MaintenanceFacadeController --> ComponentPool
MaintenanceFacadeController --> WorkshopLog
MaintenanceFacadeController --> WorkshopService
MaintenanceFacadeController --> RepairingService

@enduml
```
![controllers](img/controllers.png)