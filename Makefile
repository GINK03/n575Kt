all:
	kotlinc n575.kt -cp libs/kuromoji-core-1.0-SNAPSHOT.jar:libs/kuromoji-ipadic-1.0-SNAPSHOT.jar:libs/kuromoji-ipadic-neologd-1.0-SNAPSHOT.jar  -include-runtime -d a.jar

.PHONY:run
run:
	JAVA_OPTS="-Xmx25600M -Xms32M" kotlin -cp libs/kuromoji-core-1.0-SNAPSHOT.jar:libs/kuromoji-ipadic-1.0-SNAPSHOT.jar:libs/kuromoji-ipadic-neologd-1.0-SNAPSHOT.jar:a.jar N575Kt 
