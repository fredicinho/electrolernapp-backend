http://wiproh20-owerlen.enterpriselab.ch:8080/


GET:
api/v1/questions  -> return all questions
api/v1/questions/{ID} -> return specific question


api/v1/categories -> all categories
api/v1/categories/{ID}

### Databaseinformations

Pull Dockerimage: `docker pull mariadb`

Start Container: `docker run --name mariadb -v /home/localadmin/electrolernappDatabase/:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=electrolernapp2020 -d mariadb:10`
