# Natural 575 Detector Kotlin

## n575を検出します
ずっとなぜ人間が自然言語の文脈から575を検出できるのか不思議でした。これをなんか自動化できないでしょうか  

JVMで動く形態素解析エンジンであるKuromojiと、neologdをもちい、任意の標準入力のテキストを自動で575をスキャンします 

## requirement
```console
$ kotlinc -version
info: kotlinc-jvm 1.1.51 (JRE 1.8.0_144-b01)
```

```console
$ java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

## コンパイル
Gradleを使うほど大規模でないので、Makeでなんとかしています  
```console
$ make
```

## 実行  
```console
$ cat same-japanese-context.txt | make run
```

## サンプルの実行
「小説家になろう」の「[蜘蛛ですが、なにか？](https://ncode.syosetu.com/n7975cr/)」さんのテキストを利用しました  
```console
$ cat resource/kumodesuga | make run
猛毒に/麻痺属性を/追加して
完全に/丸投げだけど/これくらい
恩恵を/最大限に/利用して
片方が/鑑定様や/探知さん
恐怖が/私のことを/ジワジワと
攻撃の/仕様からして/自分にも
魔王でも/我ら魔族は/魔王様
本人の/意思を無視して/静止して
多分だけ/どこの世界の/言葉なん
準備は/万端という/ことですね
牽制と/たまに本気の/攻撃を
...
```

## コード
説明するほどは長くないんですが、こんな感じです  
余力があれば、BoWに変換して機械学習で、川柳らしさを計算して、その確率値が高いものだけを残すということをもできるので、暇な人はやってみるといいでしょう

```kotlin
fun main(args : Array<String>) {
  val tokenizer = Tokenizer()

  val contain = mutableListOf<MutableList<Triple<String,String,String>>>()
  BufferedReader(InputStreamReader(System.`in`)).lines()
  .forEach { 
    val oneLine = mutableListOf<Triple<String,String,String>>()
    tokenizer.tokenize(it).map { 
      Pair(it.getSurface(), it.getAllFeatures()) 
    }.map {
      val (surface, features) = it 
      var kana = features.split(",").last()
      //listOf("ァ", "ィ", "ゥ", "ェ", "ォ").map { kana = kana.replace(it, "") }
      val noun = features.split(",").first()
      oneLine.add( Triple(surface, kana, noun) )
    }
    contain.add(oneLine)
  }

  // 575が成立しているところをスキャンする
  contain.map { oneLine ->
    // separater
    val job = thread {
      val maxDepth = 100000L
      var depth = 0L
      outer@for( i in (0..oneLine.size-1) ) {
        for( k in (i+1..oneLine.size-1) ) {
          for( l in (k+1..oneLine.size-1) ) {
            for( m in (l+1..oneLine.size-1) ) {
              depth ++
              if( depth > maxDepth) break@outer
              if( oneLine.slice(i..k).map { it.second.length }.sum() == 5 && 
                  oneLine[i].third == "名詞" && oneLine[k].third == "助詞" &&
                  oneLine.slice(k+1..l).map { it.second.length }.sum() == 7 &&  
                  oneLine[k+1].third == "名詞" && oneLine[l].third == "助詞" &&
                  oneLine.slice(l+1..m).map { it.second.length }.sum() == 5 && 
                  oneLine[l+1].third == "名詞"  && (oneLine[m].third == "助詞" || oneLine[m].third == "名詞" ) && oneLine.slice(l+1..m).size > 1 ) {
                
                // validation 
                val sentence1 = oneLine.slice(i..k).map { it.first }.joinToString("") 
                val sentence2 = oneLine.slice(k+1..l).map { it.first }.joinToString("")
                val sentence3 = oneLine.slice(l+1..m).map { it.first }.joinToString("")

                val concat = sentence1 + "/" + sentence2 + "/" + sentence3
                if( sentence1.length > 5 || sentence2.length > 7 || sentence3.length > 5) continue
                if( concat.contains(",") || concat.contains("、") || concat.contains("・") ) continue
                println(sentence1 + "/" + sentence2 + "/" + sentence3)
              }
            }
          }
        }
      }
    }
    job
  }.map {
    it.join()
  }

}
```

## ライセンス
MIT
