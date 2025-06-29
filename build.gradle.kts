import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.freemarker:freemarker:2.3.34")
    }
}

plugins {
    java
}

var cfg: Configuration = Configuration(Configuration.VERSION_2_3_31)
cfg.templateLoader = FileTemplateLoader(file("./"))
cfg.defaultEncoding = "UTF-8"
val map: HashMap<String, Any> = HashMap()
map.put("title", "./")
map.put("links", ArrayList(listOf(
    HashMap(mapOf("url" to "./releases/index.html", "title" to "releases/")),
    HashMap(mapOf("url" to "./snapshots/index.html", "title" to "snapshots/")),
    HashMap(mapOf("url" to "./plugins/index.html", "title" to "plugins/"))
)))
map.put("downloads", ArrayList<HashMap<String, Any>>())
val template = cfg.getTemplate("template.ftl")
var outputDir: File = file("./")
outputDir.mkdirs()
outputDir.resolve("index.html")
    .bufferedWriter(Charsets.UTF_8)
    .use {
        template.process(map, it)
    }


val list = listOf(
    "releases",
    "plugins",
    "snapshots",
)

list.forEach {
    val tFile = file(it)
    tFile.mkdirs()
    val target = outputDir.resolve(it)
    target.mkdirs()

    forEachFile(tFile, tFile.parentFile)

}
// must be directory
fun forEachFile(f: File, o: File) {
    val name = f.relativeTo(o).toString()

    val listFiles = f.listFiles()
    val links: ArrayList<HashMap<String, Any>> = ArrayList()
    links.add(HashMap(mapOf("url" to "../", "title" to "../")))
    val downloads: ArrayList<HashMap<String, Any>> = ArrayList()
    val tMap = HashMap<String, Any>()
    @Suppress("IfThenToSafeAccess")
    if (listFiles != null) {
        listFiles.forEach {
            if (it.isDirectory) {
                val tHash = HashMap<String, Any>()
                val tName = it.toString()
                    .replace(o.toString(), "")
                    .replace("\\", "/")
                    .substring(1)
                tHash["url"] = "./$tName/index.html"
                tHash["title"] = it.name
                links.add(tHash)
            } else {
                val tHash = HashMap<String, Any>()
                val tName = it.toString()
                    .replace(o.toString(), "")
                    .replace("\\", "/")
                    .substring(1)
                tHash["url"] = "./$tName/index.html"
                tHash["title"] = it.name
                tHash["name"] = it.name
            }
        }
    }
    tMap["links"] = links
    tMap["downloads"] = downloads
    tMap["title"] = name

    val out = outputDir.resolve(name)
    out.mkdirs()
    out.resolve("index.html")
        .bufferedWriter(Charsets.UTF_8)
        .use {
            template.process(tMap, it)
        }
    @Suppress("IfThenToSafeAccess")
    if (listFiles != null) {
        listFiles.forEach {
            if (it.isDirectory)
                forEachFile(it, o)
        }
    }
}







