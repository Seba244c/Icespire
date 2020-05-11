import os

def countlines(start, lines=0, begin_start=None):
    for thing in os.listdir(start):
        thing = os.path.join(start, thing)
        if os.path.isfile(thing):
            if thing.endswith('.java'):
                with open(thing, 'r') as f:
                    newlines = f.readlines()
                    newlines = len(newlines)
                    lines += newlines

                    if begin_start is not None:
                        reldir_of_thing = '.' + thing.replace(begin_start, '')
                    else:
                        reldir_of_thing = '.' + thing.replace(start, '')

                    #file = open("./"+begin_start)
                    #print(file.read())
                    #file.close()

    for thing in os.listdir(start):
        thing = os.path.join(start, thing)
        if os.path.isdir(thing):
            lines = countlines(thing, lines, begin_start=start)

    return lines
	
mode = input("1. All programming languges\n2. Java only ")
if(mode == "2"):
	input(countlines('io/'))
else:
	input(countlines('.'))
	#E:\Code\Project Icespire\Icespire\src
	