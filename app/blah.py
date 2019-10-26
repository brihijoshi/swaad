fh = open('list_of_veggies.txt')
lov = []
for line in fh:
	line = line.split(".")[1].lstrip().rstrip()
	lov.append('"'+line.lower()+'"')

print("["+",".join(lov)+"]")