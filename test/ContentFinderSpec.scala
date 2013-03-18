import org.specs2.mutable._


import hfr._
import util.matching.Regex

class ContentFinderSpec extends Specification {

  "The ContentFinder class" should {

    "resolve urlAndPageNumber from firstPage without pageNumber" in {
      val topic = TopicRepository.getFirstTopic()
      val resolver = new PageNumberResolver(topic, None)
      val urlAndPageNumber = resolver.resolve
      val url = urlAndPageNumber._1
      val pageNumber = urlAndPageNumber._2

      url must not be equalTo(topic.url)
      pageNumber must be > (1)
    }

    "resolve urlAndPageNumber from firstPage with pageNumber" in {
      val topic = TopicRepository.getFirstTopic()
      val resolver = new PageNumberResolver(topic, Some(1000))
      val urlAndPageNumber = resolver.resolve
      val url = urlAndPageNumber._1
      val pageNumber = urlAndPageNumber._2

      url must not be equalTo(topic.url)
      pageNumber must be equalTo (1000)
    }

    "partition avatars and images" in {
      val images = List(
        "http://forum-images.hardware.fr/themes/dark/shit.gif",
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://i.minus.com/iKmav6Fr2vvBk.gif",
        "http://forum-images.hardware.fr/icones/redface.gif",
        "http://forum-images.hardware.fr/icones/redface.gif",
        "http://forum-images.hardware.fr/images/perso/mlc.gif",
        "http://i.imgur.com/XbuN4JK.gif?1",
        "http://forum-images.hardware.fr/images/perso/4/ticento.gif",
        "http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif",
        "http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif",
        "http://forum-images.hardware.fr/images/perso/ripthejacker.gif",
        "http://forum-images.hardware.fr/images/perso/ripthejacker.gif",
        "http://hfr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif"
      )
      val topic = TopicRepository.getFirstTopic
      val imagesFinder: PageImagesFinder = new PageImagesFinder(topic.url, topic.configuration)
      val rearrangeImgs = imagesFinder.rearrangeImages(images)
      val concatImgs = rearrangeImgs._1 ++ rearrangeImgs._2

      val expected = List(
        Image("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image("http://forum-images.hardware.fr/icones/redface.gif"),
        Image("http://forum-images.hardware.fr/images/perso/mlc.gif"),
        Image("http://forum-images.hardware.fr/images/perso/4/ticento.gif"),
        Image("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://hfr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6"),
        Image("http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image("http://i.imgur.com/XbuN4JK.gif?1"),
        Image("http://i.minus.com/iKmav6Fr2vvBk.gif")
      )
      concatImgs.size must be equalTo (expected.size)
      concatImgs must be equalTo (expected)
    }

    "change pageNumber from src" in {
      val pageNumber = "100"
      val url = """_[0-9]+\.""".r.replaceAllIn("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_2244.htm", "_" + pageNumber + ".")
      url must equalTo("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_100.htm")
    }

  }

}