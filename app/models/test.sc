import collection.mutable

private val tasks = mutable.Map[String, List[String]]("Mark" -> List("Make videos", "eat", "sleep", "code"))


println(tasks("Mark").patch(1,"todo",3))
println(tasks)