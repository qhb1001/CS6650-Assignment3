

# CS6650-Assignment3

https://github.com/qhb1001/CS6650-Assignment3

### 1. Database Design

In order to answer the queries efficiently, database schemas for the skier table and resort table are carefully designed. Since the data are very structured, I made use of the MySQL database in my program. 

#### 1.1. Skiers table

Primary key (skier_id, season, day_in_year, id)

| skier_id | season | day_in_year | Id                 | Lift |
| -------- | ------ | ----------- | ------------------ | ---- |
| INT      | INT    | INT         | INT AUTO_INCREMENT | INT  |

It is obvious that one skier may have several lifts in one day. So I declared an ID field to capture that property: several records may have same (`skier_id`, `season`, `day_in_year`), but with different `id` field. With the help of the above information, the following queries can be easily answered: 

-   "For skier N, how many days have they skied this season?"

    Get all the records with the given skier_id and season. Count the number of records with unique `day_in_year` field. 

-   "For skier N, what are the vertical totals for each ski day?"

    Given the skier ID, get all the records aggregated by the `day_in_year`, and then sum up all the `lift` for that day. 

-   "For skier N, show me the lifts they rode on each ski day"

    Given skier id, season and day_in_year, we can get all the lifts. 

#### 1.2 Resort table

Before the introduction of resort table, I want to mention that I made one assumption for the skier: they may only have at most one lift for any given minute. It is reasonable because it should take several minutes for one skier to finish one pass of lift. Additional, it is helpful for me to design the database because in this case I can identify one unique record by specifying the `skier_id`, `season`, `day`, and `minute`.

Primary key (resort, season, day, skier_id, minute) 

| resort | season | day_in_year | skier_id | minute | lift |
| ------ | ------ | ----------- | -------- | ------ | ---- |
| INT    | INT    | INT         | INT      | INT    | INT  |

With the help of the above information, the following queries can be easily answered:

-   "How many unique skiers visited resort X on day N?"

    Given resort id, season, and day, we can get all the lift records. Then we remove the records with deplicate skier_id, and count the number of records.

-   "How many rides on lift N happened on day N?"

    Given resort id, season, and day, we can get all the lift records. Then we count the number of records to get the answer. 

-   "On day N, show me how many lift rides took place in each hour of the ski day"

    Given resort id, season, and day, we can get the specific number of lifts with a given range of minutes, which we can compute the number of lifts in separate hours. 

### 2. Deployment topologies

One Tomcat server with t2.micro configuration. 

One RabbitMQ server with t2.micro configuration. 

One RabbitMQ consumer with t2.xlarge configuration. 

The credential information is set by the Tomcat property and java VM options. 

### 3. Part 1 - Skier Microservice

#### 3.1 128 threads

Configurations: 

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 60 connections
    *   number of threads = 50

![part1-client(128)-consumer(50)-2](figures/part1-client(128)-consumer(50)-2.png)

![part1-client(128)-consumer(50)-1](figures/part1-client(128)-consumer(50)-1.png)

#### 3.2 256 threads

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 100 connections
    *   number of threads = 170

![part1-client(256)-consumer(128)](figures/part1-client(256)-consumer(128)-success.png)

![part1-client(256)-consumer(128)](figures/part1-client(256)-consumer(128).png)

### 4. Part 2 - Resort Microservice

#### 4.1 128 threads

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 100 connections
    *   number of threads = 170

![part2-client(128)-consumer(170)-1](figures/part2-client(128)-consumer(170)-1.png)

![part2-client(128)-consumer(170)-2](figures/part2-client(128)-consumer(170)-2.png)

#### 4.2 256 threads

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 100 connections
    *   number of threads = 170

![part2-client(256)-consumer(170)-1](figures/part2-client(256)-consumer(170)-1.png)

![part2-client(256)-consumer(170)-1](figures/part2-client(256)-consumer(170)-2.png)

### 5. Part 3 - With Both Microservices

#### 5.1 128 threads

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 100 connections
    *   number of threads = 170

![part3-client(128)-consumer(170)-1](figures/part3-client(128)-consumer(170)-1.png)

![part3-client(128)-consumer(170)-2](figures/part3-client(128)-consumer(170)-2.png)

#### 5.2 256 threads

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 100 connections
    *   number of threads = 170

![part3-client(256)-consumer(170)-1](figures/part3-client(256)-consumer(170)-1.png)

![part3-client(256)-consumer(170)-2](figures/part3-client(256)-consumer(170)-2.png)

### 6. Analyst

I did encounter one abnormal pattern in the RabbitMQ management window. 

![part1-client(256)-consumer(128)-failed](figures/part1-client(256)-consumer(128)-failed.png)

The above pattern appears under the configuration: 

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 60 connections
    *   number of threads = 50

Appearantly, the RabbitMQ consumer is not competent to handle so many requests sent from the server. Firstly, the messages accumulate in the queue so that the size of the queue keeps growing (red line at 10:20:00). Then the RabbitMQ has to write those messages to the disk so that the new messages can be pushed into the queue (disk write line at 10:25:10). Even with the help of the writing technique, the size of the queue still keeps growing, those messages have to be dropped because the disk and memory resources are limited (Unroutable line at 10:25:20). 

In order to solve this problem, I reviewed the configuration of the RabbitMQ consumer, and realized that the number of threads is too low. It is even smaller than the number of database connections. After several times of experiments, I got one very robust configuration: 

*   Tomcat server

    *   default number of threads

    *   Channel pool with total 32 channels is used. 

*   RabbitMQ consumer 

    *   Database connection pool with maximum of 100 connections
    *   number of threads = 170

With the help of that, I successfully got the working RabbitMQ. 

![part1-client(256)-consumer(128)-success](figures/part1-client(256)-consumer(128)-success.png)