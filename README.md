# job4j_grabber
[![Build Status](https://app.travis-ci.com/Jazzik42/job4j_grabber.svg?branch=master)](https://app.travis-ci.com/Jazzik42/job4j_grabber)
[![codecov](https://codecov.io/gh/Jazzik42/job4j_grabber/branch/master/graph/badge.svg?token=G4E83PA66R)](https://codecov.io/gh/Jazzik42/job4j_grabber)

1. Проект представляет собой парсер вакансий. Парсинг вакансий осуществляется с сайта: https://www.sql.ru/forum/job-offers. В проект также можно добавить новые сайты без изменения кода.
2. Все распарсенные вакансии преобразуются в модели данных и хранятся в БД PostgreSQL.
3. Система запускается по расписанию, (данная функция реализована с помощью планировщика заданий Quartz), т. е. по истечению каждого периода запуска парсятся 3 страницы сайта вакансий и с помощью JDBC складируются в БД. Период запуска указывается в настройках - в файле app.properties.
