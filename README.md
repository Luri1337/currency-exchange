# 💱 Currency Exchange

A web application for managing currencies and calculating conversions. Allows:

- add new currencies,
- create currency pairs and exchange rates,
- edit existing courses,
- perform accurate calculations when converting to current courses.

, **Deployment:** http://46.8.68.247:8080/

---

## 🚀 Features

- 📥 Adding currencies
- 🔄 Currency pair management
- ✏️ Course editing
- 📊 Conversion calculation accurate to kopecks
- 🌐 REST API for working with currencies, exchange rates, and calculations

---

## 🧱 Technology stack

- **Java 21**
- **Maven**
- **Java Servlet (Jakarta 6.1.0)**
- **SQLite 3** 
- **HikariCP** — connection pool
- **Jackson** — for JSON serialization
- **Lombok** — template code reduction

---

## ⚙️ Build and launch

### 🔧 Requirements

- Java 21+
- Maven 3.6+
- Apache Tomcat 10+
- SQLite 3 

- if u wanna run, instsll all from requirements and clone from git

---

## How to use

- GET /currencies (Getting a list of currencies)
```
[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

- GET /currency/ (Getting a specific currency)
```
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

- POST /currencies (Adding a new currency to the database)
Also, you must specify name, code, sign
```
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

- GET /exchange Rates (Getting a list of all exchange rates)
```
[
    {
        "id": 0,
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
```
- GET /exchangeRate/ (Getting a specific exchange rate)
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 2,
        "name": "Russian Ruble",
        "code": "RUB",
        "sign": "₽"
    },
    "rate": 80
}
```
- POST /exchangeRates (The data is transmitted in the request body as form fields (x-www-form-urlencoded). The form fields are base Currency Code, target Currency Code, rate)
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

- PATCH /exchangeRate/ (Updating the existing exchange rate in the database)
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 2,
        "name": "Russian Ruble",
        "code": "RUB",
        "sign": "₽"
    },
    "rate": 80
}
```
- GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT

Calculation of the transfer of a certain amount of funds from one currency to another.   Request example - GET /exchange?from=USD&to=AUD&amount=10
```
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A$"
    },
    "rate": 1.45,
    "amount": 10.00,
    "convertedAmount": 14.50
}
```
