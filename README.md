http://wiproh20-owerlen.enterpriselab.ch:8080/


GET:
`api/v1/questions`  -> return all questions
`api/v1/questions/{ID}` -> return specific question

`api/v1/categories` -> all categories
`api/v1/categories/{ID}`

`api/v1/statistics/{UserID}` -> return all statistics from user
`api/v1/statistics/{QuestionID}` -> return all statistics from question (available for user)

PUT 
`api/v1/statistics` 
payload: Date, PointsAchieved, UserID, QuestionID, Marked 


### Docker
Create Image:
`docker build -t springio/gs-spring-boot-docker .`

Start Container:
`docker run -p 8080:8080 --network springbootbackend_default springio/gs-spring-boot-docker`

### Databaseinformations

Pull Dockerimage: 

`docker pull mariadb`

Start Container: 

`docker run --name mariadb -v /home/localadmin/electrolernappDatabase/:/var/lib/mysql --network springbootbackend_default -e MYSQL_ROOT_PASSWORD=electrolernapp2020 -d mariadb:10`
