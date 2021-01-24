Компиляция:

javac -cp lib/jcommander-1.78.jar -d target src/java/ru/kpfu/itis/misafin/aivar/*/*.java

-перенести файлы jcommander'a 

jar -cvfm picture-downloader.jar ../src/manifest.txt .

Пример работы программы:

java -jar picture-downloader.jar --mode=multi-thread --count=2 --files='URL;URL' --folder=/home/Pictures

java -jar picture-downloader.jar --mode=one-thread --files='URL;URL' --folder=/home/Pictures

';' - URL-разделитель.

--files='в кавычках'

java -jar picture-downloader.jar --mode=one-thread --files='https://yt3.ggpht.com/a/AATXAJwZ5LiR4piZ2k9B_-Sl7lu5wWXofQ0fxSvcXNAk=s900-c-k-c0x00ffffff-no-rj;https://im0-tub-ru.yandex.net/i?id=b398b31a27995a7550b452ae1096f6df&n=13' --folder=/home/aivar/Pictures
