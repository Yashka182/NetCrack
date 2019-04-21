package ru.skillbench.tasks.basics.entity;

public class LocationImpl implements Location{

    private String name;
    private Type type;
    private Location parent;

    /**
     * @return Название места
     */
    @Override
    public String getName() {

        return name;
    }

    /**
     * @param name Название места
     */
    @Override
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return Тип места
     * @see Type
     */
    @Override
    public Type getType() {
        return this.type;
    }

    /**
     * @param type Тип места
     */
    @Override
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @param parent "Родительское" место - то, чьей частью является данное место.
     *               Например, если данное место - это улица, то родительским местом может быть район или город.
     */
    @Override
    public void setParent(Location parent) {
        this.parent = parent;
    }

    /**
     * @return Название "родительского" места, частью которого является данное место.
     * Если родительское место не задано (равно null), метод возвращает строку "--".
     */
    @Override
    public String getParentName() {
        if(this.parent == null){
            return "--";
        }else{
            return this.parent.getName();
        }

    }

    /**
     * Возвращает место верхнего уровня, т.е. вершину иерархии, в которую входит данное место.<br/>
     *
     * @return Место, отличное от <code>null</code>. Если выше в иерархии больше нет мест, возвращает данное место.
     */
    @Override
    public Location getTopLocation() {

        Location top = new LocationImpl();
        Location currentLocation = this.parent;
        if(((Location) currentLocation) == null){
            return this;
        }
        top = currentLocation.getTopLocation();
        return top;
    }

    /**
     * Проверяет иерархию родительских мест на соответствие их типов: например, город не должен быть частью дома или города.<br/>
     * Пропуски в иерархии допустимы: например, улица может находиться в городе (не в районе), а город - в стране (не в области).
     *
     * @return true если каждое следующее (более высокое) место в иерархии имеет {@link #getType()} меньше, чем у предыдущего места.<br/>
     * Обратите внимание: {@link Type}, как любой enum, реализует интерфейс {@link Comparable},
     * и порядок определяется порядком записи значений в коде enum.
     */

    //((LocationImpl) cl).type.compareTo(((LocationImpl) cl).parent.getType())<0
    @Override
    public boolean isCorrect() {

        boolean result = true;
        Location cl = this.parent;
        if(cl == null){
            return true;
        }else if(((Location) this).getType().compareTo(((Location) cl).getType()) <= 0){
            return false;
        }
        result = cl.isCorrect();
        return result;
    }

    /**
     * Адрес - это список мест, начинающийся с данного места и включающий все родительские места.
     * Элементы списка отделяются друг от друга запятой и пробелом (", ").<br/>
     * Каждый элемент списка - это:<ol>
     * <li>просто название места, если уже содержит префикс или суффикс типа, - то есть,
     * если оно заканчиваются на точку ('.') или содержит точку среди символов от начала названия до первого пробела (' ');</li>
     * <li>в противном случае - дефолтный префикс типа ({@link Type#getNameForAddress()}) и название места.
     * Под 'названием' подразумевается результат функции {@link Location#getName()}.</li>
     * </ol>
     * Пример названий с префиксом/суффиксом, удовлетворяющих условию пункта 1: "Московская обл.", "туп. Гранитный", "оф. 321".<br/>
     * Пример названий без префикса/суффикса, НЕ удовлетворяющих этому условию: "Москва", "25 к. 2"<br/>
     *
     * @return адрес, составленный из имен (и типов) всех мест, начиная с данного места до {@link #getTopLocation()}
     */
    @Override
    public String getAddress() {
        //TODO fix getAddress
        StringBuilder adress = new StringBuilder();
        StringBuilder result = new StringBuilder();

        Location cl = this.parent;

        int var = 0;
        int ind;
        if(cl == null ) {
            if (this.getName().contains(" ")) {
                ind = this.getName().indexOf(" ");
                if (this.getName().charAt(ind - 1) == '.') {
                    var = 1;
                }
            } else {
                var = 2;
            }

            if (this.getName().endsWith(".")) {
                var = 1;
            }

            if (var == 1) {
                adress.append(this.getName() + ", ");
                return this.getName();
            } else if (var == 2) {
                adress.append(this.getType().getNameForAddress() + this.getName() + ", ");
                return this.getType().getNameForAddress() + this.getName();
            }

        }
        adress.append(cl.getAddress());

        return cl.getAddress() + ", ";
    }

    public String toString(){
        return this.getName()+ " " + "(" + this.getType().toString() + ")";
    }

    public static void main(String[] args) {
        Location house = new LocationImpl();
        house.setName("13");
        house.setType(Type.BUILDING);

        Location street = new LocationImpl();
        street.setName("ул. Беломорская");
        street.setType(Type.STREET);

        house.setParent(street);

        Location city = new LocationImpl();
        city.setName("Москва");
        city.setType(Type.CITY);

        Location region = new LocationImpl();
        region.setName("Московская обл.");
        region.setType(Type.REGION);

        street.setParent(city);
        city.setParent(region);

        System.out.println(house.getAddress());

    }
}
