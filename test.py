import os

def countlines(start, lines=0, begin_start=None):
    for thing in os.listdir(start):
        thing = os.path.join(start, thing)
        if os.path.isfile(thing):
            if thing.endswith('.java') or thing.endswith('.xml'):
                with open(thing, 'r') as f:
                    for line in f.readlines():
                        if "2.10.4" in line: print(thing)

                    if begin_start is not None:
                        reldir_of_thing = '.' + thing.replace(begin_start, '')
                    else:
                        reldir_of_thing = '.' + thing.replace(start, '')

    for thing in os.listdir(start):
        thing = os.path.join(start, thing)
        if os.path.isdir(thing):
            countlines(thing, lines, begin_start=start)

	
mode = input("1. All programming languges\n2. Java only ")
if(mode == "2"):
	input(countlines('./'))
else:
	input(countlines('.'))
	#E:\Code\Project Icespire\Icespire\src
	