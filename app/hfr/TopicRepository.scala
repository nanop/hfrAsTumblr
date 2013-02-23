package hfr

import play.api.Logger
import play.api.libs.json._

case class Topic(id: String, name: String, url: String)

object Topic {

  def apply(name: String, url: String) = {
    // Id in sha1...
    // TODO: use mongodb on another nosql database to store topics and maybe more... like every images loaded...
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val id = new sun.misc.BASE64Encoder().encode(md.digest((url + name).getBytes))
    new Topic(id, name, url)
  }
}

object TopicFormats {

  implicit object TopicFormat extends Format[Topic] {

    def reads(json: JsValue): JsResult[Topic] = {
      val id = (json \ "id").as[String]
      JsSuccess(TopicRepository.findTopic(id))
    }

    def writes(topic: Topic): JsValue = {
      JsObject(
      List("id" -> JsString(topic.id),
      "name" -> JsString(topic.name)
      ))
    }

  }

}

object TopicRepository {

  import TopicFormats.TopicFormat

  def getTopics() = {
    val topics = List(
        Topic("Images étonnantes", "http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm"),
        Topic("Gifs: Femmes, Caca, Chutes&Co", "http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm"))
    topics
  }

  def getFirstTopicUrl() = getTopics().head.url

  def getTopicsAsJson() = Json.toJson(getTopics())

  def getDefaultTopic() = getTopics().head

  def findTopic(id: String): Topic = {
    val topic = getTopics().find(_.id == id).getOrElse(getDefaultTopic())
    Logger.info("Get topic %s from id %s".format(topic.name, id))
    topic
  }

  def findTopicUrl(id: String) = findTopic(id).url

}
