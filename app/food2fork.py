import requests

key = "4eac3e3af9c9d43f582508330a782573"


def get_recipes(list_ingredients):
	query_ingredients = "%20".join(list_ingredients)
	query = "https://www.food2fork.com/api/search?key="+key+"&q="+query_ingredients
	response = requests.get(query)
	return response.json()['recipes'][:8]


def main():
	list_ingredients =['garlic', 'tomato']

	print(get_recipes(list_ingredients))


if __name__ == "__main__":
    main()

