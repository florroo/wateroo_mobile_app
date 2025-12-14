Programowanie urządzeń mobilnych laboratorium L_2_ 

# Dokumentacja projetu: ** Aplikacja do nawadniania - Wateroo **

## Zespoł projetowy:
- Bartłomiej Florek

## Opis projektu
Aplikacja ma na celu przypominanie użytkownikowi o regularnym nawadnianiu. Powiadomienia będą wysyłane w postaci wibracji, błysków latarki oraz odpowiednich dźwięków, aby użytkownik nie zapomniał o regularnym piciu wody i innych napojów. Użytkownik podaje, jakie napoje wypił oraz ich ilość, a aplikacja analizuje, czy dzienne zapotrzebowanie na płyny zostało spełnione. Na koniec dnia generowany jest raport, który przedstawia składniki odżywcze (np. cukier, węglowodany) w wypitych napojach.

## Zakres projektu opis funkcjonalności:
- Powiadomienia push: aplikacja wysyła powiadomienia push, które przypominają użytkownikowi o konieczności picia płynów. Powiadomienia mogą przybierać formę: wibracji, błysków latarki, dźwięków.

- Sprawdzanie dziennego zapotrzebowania na płyny: aplikacja pozwala użytkownikowi monitorować dzienne zapotrzebowanie na płyny

- Dodawanie płynów wypitych podczas dnia: użytkownik może wprowadzać informacje o wypitych napojach (np. woda, herbata, kawa, soki) oraz ich ilości. Aplikacja oblicza, ile płynów zostało wypitych w danym dniu i porównuje to z zapotrzebowaniem.

- Sprawdzanie składników w wypitych napojach: aplikacja analizuje skład wypitych napojów, uwzględniając zawartość takich składników odżywczych jak: cukier, węglowodany, kalorie, witaminy i minerały.
Na koniec dnia użytkownik otrzymuje raport, który pokazuje, ile cukru, węglowodanów i innych składników odżywczych spożył, w oparciu o napoje, które wprowadził.

## Panele / zakładki aplikacji 
- Logowanie

![login](images/1)

- Rejestracja

![rejestracja](images/2)

- Przypominanie hasła

![forgot](images/3)

- Panel domowy użytkownika

![home](images/4)

- Panel wykresów spożytych napojów

![charts](images/5)

- Panel ustawień

![settings](images/6)

## Biblioteki
### Biloteka implementująca wykresy
- implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

### Biblioteki tworzące bazę danych
- val room_version = "2.6.1"
- implementation("androidx.room:room-runtime:$room_version")
- implementation("androidx.room:room-ktx:$room_version")
- ksp("androidx.room:room-compiler:$room_version")

### Biblioteka implementująca tworzenie powiadomien
- implementation ("androidx.work:work-runtime-ktx:2.8.0")

## Baza danych
###### Diagram ERD
![erd](https://github.com/user-attachments/assets/cd385222-3291-48dd-aa4d-491b4862aab3)

###### Opis bazy danych
Tabela "user" przechowuje informacje o użytkownikach aplikacji, w tym dane logowania oraz kontaktowe.
Kolumny tabeli:
- ID(INT) - unikalny identyfikator użytkownika, pełniący rolę klucza głównego
- login(VARCHAR(15)) - nazwa użytkownika wykorzystywana do logowania. Maksymalna długość 15 znaków
- password(VARCHAR(25)) - hasło użytkownika. Maksymalna długość 20 znaków.
- email(VARCHAR(40)) - email użytkownika, służacy w aplikacji do tworzenia konta, przypominania hasła oraz komunikacji z użytkownikiem.
- drink_amount(INT) - ilość spożytej wody rejestrowana w aplikacji.

## Wykorzystane uprawnienia aplikacji do:
- Aparat
- Latarka
- Powiadomienia
- Wibracje
