import org.specs2.mutable._


import hfr._

class ChangeUrlPageInfosSpec extends Specification {

  "The ChangeUrlPageInfos class" should {

    "replace page number for joiesducode" in {
      val token = "http://lesjoiesducode.tumblr.com/page/1"

      val changeUrlPageInfos = JoiesDuCodeConfiguration.get().pageResolverInfos.changeUrlPageInfos
      val regex = changeUrlPageInfos.regex.r
      val newToken = regex.replaceFirstIn(token, changeUrlPageInfos.replacement.format(2))

      newToken must be equalTo("http://lesjoiesducode.tumblr.com/page/2")
    }

    "replace page number for hfr" in {
      val token = "http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm"

      val changeUrlPageInfos = HfrConfiguration.get().pageResolverInfos.changeUrlPageInfos
      val regex = changeUrlPageInfos.regex.r
      val newToken = regex.replaceFirstIn(token, changeUrlPageInfos.replacement.format(999))

      newToken must be equalTo("http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_999.htm")
    }


  }

}