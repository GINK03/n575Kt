# Natural 575 Detector Kotlin

## n575を検出します
ずっとなぜ人間が自然言語の文脈から575を検出できるのか不思議でした。これをなんか自動化できないでしょうか  

JVMで動く形態素解析エンジンであるKuromojiと、neologdでもちい、任意の標準入力のテキストを自動で575をスキャンします 

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
たちスーの/前で普通に/日本語で
恩恵を/最大限に/利用して
片方が/鑑定様や/探知さん
望遠で/遠距離から/邪眼
恐怖が/私のことを/ジワジワと
攻撃の/仕様からして/自分にも
魔王でも/我ら魔族は/魔王様
本人の/意思を無視して/静止して
変影で/影の形を/槍状 
多分だけ/どこの世界の/言葉なん
準備は/万端という/ことですね
蜘蛛型の/魔物っていう/のがあるし
甲殻と/神鋼体の/防御に
牽制と/たまに本気の/攻撃を
...
```

## ライセンス
MIT
