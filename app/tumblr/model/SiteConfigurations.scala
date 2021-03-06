package tumblr.model

trait ConfigurationBuilder {
  val regexLastPage = """([0-9]+)$"""

  def getCssSelectors(): CssSelectors
  def isLastPageByCss(): Boolean = true
  def getNavigationAscending(): Boolean
  def getImageRule(): Option[ImageRule]

  def getPageNumberDescriptor(): Option[PageNumberDescriptor]
  def getChangePageDescriptor(): ChangePageDescriptor

  def getPageResolver() = new PageResolver(getPageNumberDescriptor(), getChangePageDescriptor())

  def get() = new Configuration(getCssSelectors(), isLastPageByCss(), getNavigationAscending(), getPageResolver(), getImageRule())
}

object HfrConfiguration extends ConfigurationBuilder {
  def getCssSelectors() = new CssSelectors(new CssSelector("tr.message td.messCase2 img", Some("src")), None)

  def getNavigationAscending() = false

  def getImageRule() = {
    Some(new ImageRule(exclude = "http://forum-images.hardware.fr/themes", startsWith = List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")))
  }

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("tr.cBackHeader.fondForum2PagesHaut div.left a:last-child", Some("href")), """([0-9]+)\.htm"""))
  def getChangePageDescriptor() = new ChangePageDescriptor("""_[0-9]+\.""", "_%d.")
}

trait TumblrConfiguraiton extends ConfigurationBuilder {
  def getCssSelectors() = new CssSelectors(new CssSelector(".post .bodytype img", Some("src")), Some(new CssSelector(".post h3 a", Some("href"))))

  def getNavigationAscending() = true

  def getImageRule() = None

  def getChangePageDescriptor() = new ChangePageDescriptor("""\/page\/[0-9]+""", "/page/%d")
}

object JoiesDuCodeConfiguration extends TumblrConfiguraiton {
  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".footer i"), regexLastPage))
}

object JoiesDuSysadminConfiguration extends TumblrConfiguraiton {
  def getPageNumberDescriptor() = Some( new PageNumberDescriptor(new CssSelector(".pagination"), regexLastPage))
}

object JoiesDuScrumConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post p img", Some("src")), Some(new CssSelector(".post a h3", Some("href"))))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("#prev-next"), regexLastPage))
}

object JoiesDuTestConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post p img", Some("src")), Some(new CssSelector(".post h3 a", Some("href"))))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".page-number"), regexLastPage))
}

object ChersVoisinsConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("#page-location"), regexLastPage))
}


object CommitStripConfiguration extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".entry-content img", Some("src")), Some(new CssSelector(".post h1 a")))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), regexLastPage))
}

object DataAnxietyConfiguration extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".post-content img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), regexLastPage))
}

object SportBallsReplacedWithCatsConfiguration extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".photo_post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), regexLastPage))
}

object DontForgetCondomConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".pagination .count"), regexLastPage))
}

object FailBlogFrConfiguration extends ConfigurationBuilder {

  def getNavigationAscending() = true

  def getImageRule() = None

  def getChangePageDescriptor() = new ChangePageDescriptor("""/page-[0-9]+.html""", "/page-%d.html")

  override def getCssSelectors() = new CssSelectors(new CssSelector(".contenu a img", Some("src")), Some(new CssSelector(".contenu h1 a", None)))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".page p > a:last-child"), regexLastPage))
}

object ActressesWithoutTeeth extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".content .photo img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), """([0-9]+)$"""))
}