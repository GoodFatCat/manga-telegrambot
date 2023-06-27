# Manga telegram bot
This project sends notifications to users about new manga from website mangalib.me via telegram bot

## Deployment
Deployment process as easy as possible:
Required software:
- terminal for running bash scripts
- docker
- docker-compose

To deploy application, switch to needed branch, change ${BOT_NAME} and ${BOT_TOKEN} to needed values 
in application.properties and application-test.properties and run bash script:

$ bash start.sh

That's all.