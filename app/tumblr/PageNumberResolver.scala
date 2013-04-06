package tumblr

import model._

import jsoup.DocumentWrapper

case class PageNumberResolver(site: Site, pageNumber: Option[Int] = None) {

  val LinkHrefAttribute = "href"

  def resolve(): (String, Int) = {
    val pageIndex = pageNumber match {
      case None => getPageNumber()
      case Some(pageNumber: Int) => pageNumber
    }

    val pageUrl = getPageUrl(pageIndex)
    (pageUrl, pageIndex)
  }

  def getPageUrl(pageNumber: Int) = {
    val resolver: PageResolver = site.configuration.pageResolver
    val changeUrlPageDescriptor: ChangePageDescriptor = resolver.changePageDescriptor
    val pageUrl = changeUrlPageDescriptor.regex.r.replaceFirstIn(site.url, changeUrlPageDescriptor.replacement.format(pageNumber))

    pageUrl
  }

  def getPageNumber(): Int = {
    if(site.configuration.navigationOrder == NavigationOrder.Ascending) {
      1
    } else {
      getLastPageNumber()
    }
  }

  def getLastPageNumber(): Int = {
    val optionPageNumberDescriptor: Option[PageNumberDescriptor] = site.configuration.pageResolver.pageNumberDescriptor
    optionPageNumberDescriptor match {
      case None => 1
      case Some(pageNumberDesc) => {
        val wrapper: DocumentWrapper = new DocumentWrapper(site.url)
        val selector: CssSelector = pageNumberDesc.cssSelector

        val elements = wrapper.list(selector.cssQuery, selector.htmlAttribute)
        if (elements.isEmpty) 1
        else {
          val url = elements(0)
          val result = pageNumberDesc.regex.r.findFirstMatchIn(url).get
          result.group(1).toInt
        }
      }
    }
  }

}