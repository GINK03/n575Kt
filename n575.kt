import com.atilika.kuromoji.ipadic.neologd.Tokenizer
import com.atilika.kuromoji.ipadic.neologd.Token
import com.atilika.kuromoji.ipadic.Tokenizer.Builder
import java.io.*
import kotlin.concurrent.*
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
      //println(features)
      val kana = features.split(",").last()
      val noun = features.split(",").first()
      oneLine.add( Triple(surface, kana, noun) )
    }
    contain.add(oneLine)
  }

  // 575が成立しているところをスキャンする
  contain.map { oneLine ->
    // separater
    val job = thread {
      val maxDepth = 1000000L
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
                println( oneLine.slice(i..k).map { it.first }.joinToString("") + "/" + 
                        oneLine.slice(k+1..l).map { it.first }.joinToString("") + "/" +
                        oneLine.slice(l+1..m).map { it.first }.joinToString("") )
                //println( oneLine.slice(i..k).map { it.third } )
                //println( oneLine.slice(k+1..l).map { it.third } )
                //println( oneLine.slice(l+1..m).map { it.third } )
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
