package search
import java.io.File

class Notebook {
    //var numberOfPersons = 0

    val notebook: MutableList<Person> = mutableListOf()
    val notebookInvertedIndex = mutableMapOf<String, MutableList<Int>>()


    data class Person(val firstName: String, val lastName: String?=null, val email: String?=null) {
        override fun toString(): String {
            if (email == null) return "$firstName $lastName"
            else return "$firstName $lastName $email"
        }
    }


    fun addPerson(line: List<String>) {
        when (line.size) {
            3 -> {
                this.notebook.add(Person(line[0], line[1], line[2]))
                noteToInvertedIndex(this.notebook.size-1, line[0], line[1], line[2])
            }
            2 -> {
                this.notebook.add(Person(line[0], line[1]))
                noteToInvertedIndex(this.notebook.size-1, line[0], line[1])
            }
            1 -> {
                this.notebook.add(Person(line[0]))
                noteToInvertedIndex(this.notebook.size-1, line[0])
            }
        }
    }

    fun findPerson(value: String) {
        val listOfseach: MutableList<String> = mutableListOf()
        var i = 0
        for (person in this.notebook) {
            if (value.lowercase() in person.firstName.lowercase() //person.firstName.lowercase() == value.lowercase()
                || person.lastName != null && value.lowercase() in person.lastName?.lowercase() // person.lastName?.lowercase() == value.lowercase()
                || (person.email != null && value.lowercase() in person.email) ) { // || value.lowercase() in person.email.lowercase()
                listOfseach.add(person.toString())
            }
        }
        if (listOfseach.size == 0) {
            println("No matching people found.")
        }
        else {
            //println()
            //println("People found:")
            println("${listOfseach.size} person found:")
            for (i in listOfseach) println(i)
        }
    }

    fun findPersonInInvertedIndex(value: String, stragedy: String) {
        when (stragedy) {
            "ALL" -> {
                var indexSearch: Int
                val items = value.split(" ")
                if (items[0] in notebookInvertedIndex && items.size == 2) {
                    for (i in notebookInvertedIndex[items[0]]!!) {
                        if (i in notebookInvertedIndex[items[1]]!!) {
                            println("1 person found:")
                            return println(notebook[i])
                        }
                        }
                } else {
                    println("No matching people found.")
                }
            }
            "ANY" -> {
                var listSearch = mutableSetOf<Int>()
                var iSub: String = ""
                for (i in value.split(" ")) {
                    if ('@' !in i) iSub = i[0].uppercase() + i.substring(1).lowercase() else iSub = i
                    //println(i[0].uppercase() + i.substring(1).lowercase())
                    if (iSub in notebookInvertedIndex) listSearch.addAll(notebookInvertedIndex[iSub]!!)
                }
                if (listSearch.size > 0) {
                    println()
                    println("${listSearch.size} person found:")
                    for (i in listSearch) println(notebook[i])
                }
                else println("No matching people found.")
            }
            "NONE" -> {
                var listSearch = mutableSetOf<Int>()
                var iSub: String = ""
                for (i in value.split(" ")) {
                    if ('@' !in i) iSub = i[0].uppercase() + i.substring(1).lowercase() else iSub = i
                    if (iSub in notebookInvertedIndex) listSearch.addAll(notebookInvertedIndex[iSub]!!)
                }
                if (listSearch.size > 0) {
                    println()
                    println("${notebook.size - listSearch.size} person found:")
                    for (i in 0..notebook.size-1) if (i !in listSearch) println(notebook[i])
                }
                else println("No matching people found.")
            }
        }

    }

    fun listOfPeople() {
        for (person in this.notebook) {
            println(person)
        }
    }

    fun noteToInvertedIndex(i: Int, firstName: String, lastName: String? = null, email: String?= null) {

        if (!this.notebookInvertedIndex.containsKey(firstName)) {
            this.notebookInvertedIndex[firstName] = mutableListOf(i)
        }
        else {
            this.notebookInvertedIndex[firstName]!!.add(i)
        }
        if (!this.notebookInvertedIndex.containsKey(lastName)) {
            if (lastName != null) this.notebookInvertedIndex[lastName] = mutableListOf(i)
        }
        else {
            if (lastName != null) this.notebookInvertedIndex[lastName]!!.add(i)
        }
        if (!this.notebookInvertedIndex.containsKey(email)) {
            if (email != null) this.notebookInvertedIndex[email] = mutableListOf(i)
        }
        else {
            if (email != null) this.notebookInvertedIndex[email]!!.add(i)
        }

    }
}

fun menu(value: String, note: Notebook) {
    when (value) {
        "1" -> {

            println("Select a matching strategy: ALL, ANY, NONE")
            val stragedy = readln()
            println()
            println("Enter a name or email to search all matching people.")
            //note.findPerson(readln())
            note.findPersonInInvertedIndex(readln(), stragedy)
        }
        "2" -> {
            println("=== List of people ===")
            note.listOfPeople()
        }
        "0" -> {
            //println()
            println("Bye!")
        }
        else -> println("Incorrect option! Try again.")
    }
}

fun main(args: Array<String>) {
    val note = Notebook()
    when (args[0]) {
        "--data" -> {
            val inputFile = File(args[1])
            inputFile.forEachLine {
                note.addPerson(it.split(" ").toList())
            }
        }
    }
    //println(note.notebookInvertedIndex)
    do {
        println()
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
        val y = readln()
        println()
        menu(y, note)
    } while (y != "0") // y is visible here!
}





