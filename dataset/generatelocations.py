# generates locations.txt for use by our android map app
import xlrd 

output = ''
name_to_abbr = {}
workbook = xlrd.open_workbook('dataset.xlsx')

building_sheet = workbook.sheet_by_name('buildings')
curr_row = 0
while curr_row < building_sheet.nrows - 1:
	abbr = building_sheet.cell(curr_row, 0).value
	name = building_sheet.cell(curr_row, 1).value
	x = building_sheet.cell(curr_row, 4).value
	y = building_sheet.cell(curr_row, 5).value

	if name not in name_to_abbr:
		name_to_abbr[name] = abbr

	# todo: include floors...
	# cast to int to remove decimals, then to string to write
	output += 'location ' + abbr + ' ' + str(int(x)) + ' ' + str(int(y)) + '\n;\n\n' 

	curr_row += 1

passive_locs_sheet = workbook.sheet_by_name('passive')
curr_row = 0
while curr_row < passive_locs_sheet.nrows - 1:
	abbr = passive_locs_sheet.cell(curr_row, 0).value
	name = passive_locs_sheet.cell(curr_row, 1).value
	x = passive_locs_sheet.cell(curr_row, 3).value
	y = passive_locs_sheet.cell(curr_row, 4).value

	if name not in name_to_abbr:
		name_to_abbr[name] = abbr

	output += 'passive_location ' + abbr + ' ' + str(int(x)) + ' ' + str(int(y)) + '\n'

	curr_row += 1
output += '\n'

paths_sheet = workbook.sheet_by_name('paths')
curr_row = 0
while curr_row < paths_sheet.nrows - 1:
	if (paths_sheet.cell(curr_row, 0).value):
		from_abbr = name_to_abbr[paths_sheet.cell(curr_row, 0).value]
		to_abbr = name_to_abbr[paths_sheet.cell(curr_row, 1).value]
		output += 'path ' + from_abbr + ' ' + to_abbr + '\n'

	if (paths_sheet.cell(curr_row, 3).value):
		x = paths_sheet.cell(curr_row, 3).value
		y = paths_sheet.cell(curr_row, 4).value

		output += '\tp ' + str(int(x)) + ' ' + str(int(y)) + '\n'

	if (not paths_sheet.cell(curr_row, 0).value and not paths_sheet.cell(curr_row, 3).value):
		output += ';\n\n'

	curr_row += 1

print output
		
