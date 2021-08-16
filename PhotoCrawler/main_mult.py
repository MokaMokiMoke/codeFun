from requests import get
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import numpy as np
from PIL import Image
import glob, sys, os.path, time, threading

class hp(object):
    waitTime = 3.2
    savefile = 'autosave.txt'
    id = 192771

def do(start, end):
    for i in range(start, end):
        print("Current ID: " + str(i))
        if os.path.exists("img/" + str(i) + ".jpg"):
            print("ID: " + str(i) + " already downloaded, skpping")
            continue  
        fetchSlices(i)
        stichSlices(i)

def alt():
    threads = []
    for i in range(5000, 5200, 50):
        thread = threading.Thread(target=do, args=(i, i+50))
        threads.append(thread)
        thread.start()

    for thread in threads:
        thread.join()
    print('Done.')



def download(url, file_name):
    # open in binary mode
    with open(file_name, "wb") as file:
        # get request
        response = get(url)
        # write to file
        file.write(response.content)

def stichSlices(id):

    if os.path.exists(str(id) + "_single.jpg"):
        return

    quality_val = 95
    list_im = glob.glob(str(id) + '_*.jpg')
    imgs    = [ Image.open(i) for i in list_im ]
    min_shape = sorted( [(np.sum(i.size), i.size ) for i in imgs])[0][1]
    imgs_comb = np.hstack( (np.asarray( i.resize(min_shape) ) for i in imgs ) )
    imgs_comb = Image.fromarray( imgs_comb)
    imgs_comb.save(str(id) + "_merged.jpg", 'JPEG', quality=quality_val)

    
    if os.path.exists(str(id) + "_merged.jpg"):
        if os.path.getsize(str(id) + "_merged.jpg") > (1024 * 20):
            for i in range(4):
                os.remove(str(id) + "_" + str(i) + ".jpg")
            os.rename(str(id) + "_merged.jpg", "./img/" + str(id) + ".jpg") 

def fetchSlices(id):

    options = webdriver.ChromeOptions()
    options.binary_location = r"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe"
    chrome_driver_binary = r"D:\Max\Downloads\code\chromedriver.exe"
    driver = webdriver.Chrome(chrome_driver_binary, chrome_options=options)
    #print(type(driver))
    driver.get("https://myHomepage.com/photo/" + str(id))
    button = driver.find_element_by_id('mainphoto')
    button.click()
    driver.implicitly_wait(hp.waitTime) #seconds
    slices = driver.find_elements_by_xpath('//*[@id="quick_container"]/div/img')

    if slices.__len__() == 0: # no Slices found (probably not enlargable)
        mainphoto = driver.find_element_by_xpath('//*[@id="mainphoto"]/img')
        download(mainphoto.get_attribute("src"), str(id) + "_single.jpg")
        return

    ctr = 0
    for slice in slices:
        filename = str(id) + "_" + str(ctr) + ".jpg"
        url = slice.get_attribute("src")
        download(url, filename)
        ctr += 1
    driver.quit()
    time.sleep(1.5) # wait till all files are downloaded

alt()
