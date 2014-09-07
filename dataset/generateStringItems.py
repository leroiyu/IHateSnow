file = open('lnames.txt','r')
array = file.read().split('\n')

output = ''
for item in array:
	output += '<item>' + item + '</item>\n'

for 

print output