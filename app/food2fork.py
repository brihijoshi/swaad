import requests

key = "7a7b5d0167d9c87792acaff78bf895b0"

def get_recipes(list_ingredients):
	query_ingredients = "%20".join(list_ingredients)
	query = "https://www.food2fork.com/api/search?key="+key+"&q="+query_ingredients
	response = requests.get(query)
	return response.json()['recipes'][:5]


def main():
	list_ingredients =['garlic','tomato']

	print(get_recipes(list_ingredients))

if __name__ == "__main__":
    main()

