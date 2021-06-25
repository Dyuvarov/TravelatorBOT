FROM openjdk:8
ADD target/TravelatorBot.jar TravelatorBot.jar
ADD answerExamples/answ.txt answerExamples/answ.txt
#ADD answerExamples/hotelAnsw answerExamples/hotelAnsw
ADD answerExamples/hotelAnsw2org answerExamples/hotelAnsw2org
ENTRYPOINT ["java", "-jar", "TravelatorBot.jar"]