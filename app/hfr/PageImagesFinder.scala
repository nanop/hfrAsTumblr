package hfr

import jsoup.DocumentWrapper
import collection.mutable.ListBuffer

case class PageImagesFinder(url: String, configuration: Configuration) {

  val wrapper: DocumentWrapper = new DocumentWrapper(url)
  val cssSelectors = configuration.cssSelectors

  def find() = {
    val images = zipImagesAndTexts()
    rearrangeImages(images)
  }

  def zipImagesAndTexts() = {
    var buffer: ListBuffer[Image] = ListBuffer()

    val combinaison = listImages().zipAll(listTexts(), "", "")
    combinaison.foreach(comb => {
      buffer += new Image(comb._1, comb._2)
    })

    buffer.toList
  }

  def listImages() = {
    wrapper.listAttribute(cssSelectors.images.cssQuery, cssSelectors.images.htmlAttribute)
  }

  def listTexts() = {
    val texts: List[String] = cssSelectors.text match {
      case None => List()
      case Some(cssSelector: CssSelector) => {
        wrapper.listText(cssSelector.cssQuery)
      }
    }
    texts
  }

  def rearrangeImages(images: List[Image]): (List[Image], List[Image]) = {
    val distinct = images.distinct

    val imageRule = configuration.imageRule
    imageRule match {
      case None => (List(), distinct)
      case Some(rule: ImageRule) => {
        val rearrange = distinct
          .filterNot(_.src.startsWith(rule.exclude))
          .partition(i => rule.firstsStartsWith.exists(i.src.startsWith))

        (rearrange._1, rearrange._2.reverse)
      }
    }
  }


}