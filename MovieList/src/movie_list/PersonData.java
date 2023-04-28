package movie_list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PersonData implements Data<String, Person>, Iterable<Person> {
    private HashMap<String, Person> personDatabase;

    public PersonData(){
        personDatabase = new HashMap<String, Person>();
    }

    public boolean add(Person person){
        if(personDatabase.containsKey(person.getName())) return false;
        personDatabase.put(person.getName(), person);
        return true;
    }

    public boolean delete(String personName){
        if(personDatabase.remove(personName) == null) return false;
        return true;
    }

    public Person search(String personName){
        return personDatabase.get(personName);
    }

    public boolean checkIfExist(String personName){
        return personDatabase.containsKey(personName);
    }

    @Override
    public Iterator<Person> iterator(){
        Iterator<Person> iterator;
        iterator = new Iterator<Person>(){
            Iterator<Map.Entry<String, Person>> mapIterator = personDatabase.entrySet().iterator();

            @Override
            public boolean hasNext(){
                return mapIterator.hasNext();
            }

            @Override
            public Person next(){
                return mapIterator.next().getValue();
            }
        };
        return iterator;
    }
}
