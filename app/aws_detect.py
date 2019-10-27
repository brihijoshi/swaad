import boto3

list_of_veggies = ["arugula", "apple", "acorn squash", "asian pear", "asparagus", "acai", "butternut squash",
                   "alfalfa sprouts", "artichoke", "avocado", "banana", "broccoli", "bok choy", "blueberries",
                   "brussel sprouts", "blackberries", "black currants", "beets", "beans", "blood orange",
                   "boysenberries", "banana squash", "bell peppers", "green pepper", "yellow pepper", "orange pepper",
                   "purple pepper", "brown pepper", "cantalope", "corn", "cabbage", "napa cabbage", "red cabbage",
                   "carrot", "purple carrot", "cherries", "purple cauliflower", "cauliflower", "orange cauliflower",
                   "celery", "coconut", "cranberries", "cucumber", "crookneck squash", "chives", "swiss chard",
                   "dragon fruit", "eggplant", "edamame", "eight-ball squash", "endive", "escarole", "elephant garlic",
                   "figs", "fennel", "garlic", "green onions", "green peas", "green beans", "grapes", "ginger",
                   "grapefruit", "garbanzos pedrosillano", "honeydew melon", "horned melon", "huckleberries",
                   "red huckleberries", "iceberg lettuce", "jalapeno", "jicama", "kale", "key lime", "kidney beans",
                   "kumquat", "lentils", "limes", "lemon", "green leaf lettuce", "red leaf lettuce", "oak leaf lettuce",
                   "leeks", "meyer lemon", "mango", "champagne mango", "mint", "mandarins", "mushroom", "mushrooms",
                   "nectarine", "white necterine", "orange", "olive", "okra", "onions", "onion", "red onions", "oyster",
                   "greek oregano", "peach", "white peach", "pears", "pear", "pineapple", "portabella", "red potatoes",
                   "purple potatoes", "parsnips", "potato", "parsley", "pomegranate", "pumpkin", "papaya", "papya",
                   "pinto beans", "quince", "patty pan squash", "quinoa", "radish", "white radish", "rhubarb",
                   "rosemary", "raspberry", "wild raspberry", "seaweed", "spinach", "sugar snap peas", "snow pea",
                   "salmon berries", "summer squash", "spaghetti", "tomato", "squash", "tomatoes", "tomatoes - roma",
                   "tomatoes - cherry", "yellow tomatoes", "green tomato", "heirloom tomatoes", "orange tomato",
                   "kumato tomato", "yellow pear tomato", "tomatillo", "tangerine", "lemon thyme", "uzouza leaf",
                   "vanilla", "watermelon", "watercress", "wild rice", "xigua", "yams", "yuzu", "zucchini",
                   "grey zucchini", "pears", "apricot", "strawberries", "strawberry", "cantaloupe", "lemons", "peaches",
                   "oranges", "honeydew", "plums", "apricots", "bearberry", "lime", "clementines", "red grapes",
                   "white grapes", "pomelo", "raspberries", "kiwi", "plum", "litchi", "longan", "starfruit", "durian",
                   "abiu", "kumquats", "acerola", "mango", "ackee", "mangosteen", "elderberrys", "boysenberry",
                   "bilberry", "red bayberry", "barberry", "buffaloberry", "black cherry", "plum lemon",
                   "black raspberry", "mulberries", "broad-leaf bramble", "babaco", "bael", "barbadine", "bilimbi",
                   "bitter gourd", "bitter melon", "black sapote", "guava", "blackcurrants", "bignay", "beech nut",
                   "dates", "damson", "date plum", "duku", "prunes", "persimmon", "juniper berry", "jujube", "kapok",
                   "gooseberry", "tomato", "youngberry", "atemoya", "cempedak", "jackfruit", "prickly pear"]


def detect_labels_local_file(photo):
    client = boto3.client('rekognition')
    print(str(client))

    with open(photo, 'rb') as image:
        print('opening file')
        response = client.detect_labels(Image={'Bytes': image.read()})
        print(response)
    # print('Detected labels in ' + photo)    
    for label in response['Labels']:
        if label['Name'].lower() in list_of_veggies:
            return label['Name'].lower()

    return None


def main():
    photo = '/Users/johnteng/Downloads/carrots.jpeg'
    # photo ='onions-fridge.jpg'

    label = detect_labels_local_file(photo)

    print(label)

    return label


if __name__ == "__main__":
    main()
