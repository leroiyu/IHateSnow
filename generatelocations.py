# generates locations.txt for use by our android map app
import xlrd 

output = ''

workbook = xlrd.open_workbook('dataset.xlsx')

building_sheet = workbook.sheet_by_name('building')
curr_row = 0
while curr_row < building.nrows - 1:
	abbr = building_sheet.cell(curr_row, 0)
	x = building_sheet.cell(curr_row, 4)
	y = building_sheet.cell(curr_row, 5)

	# todo: include floors...
	output += 'location "' + abbr + ' ' + x + ' ' + y + '\n;' 

	curr_row += 1

print output

