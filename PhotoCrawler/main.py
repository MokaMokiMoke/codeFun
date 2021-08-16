from requests import get
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import numpy as np
from PIL import Image
import glob, sys, os.path, time
import shutil

class tg(object):
    waitTime = 3.2
    savefile = 'autosave.txt'
    id = 123456

def main():
    loadAutosave()
    while tg.id > 0:
        print("ID: " + str(tg.id))
        if os.path.exists("Z:\\Downloads\\hp\\" + str(tg.id) + ".jpg") or os.path.exists("Z:\\Downloads\\hp\\" + str(tg.id) + "_single.jpg"):
                print("ID: " + str(tg.id) + "already downloaded, skipping")
                td.id -= 1
                autosave(tg.id)
                continue
        fetchSlices(tg.id)
        stichSlices(tg.id)
        tg.id -= 1
        autosave(tg.id)

def loadAutosave():
    if os.path.exists(tg.savefile):
        with open(tg.savefile) as f:
            tg.id = int(f.readline())


def autosave(id):
    with open(tg.savefile, "w") as autoFile:
        autoFile.write(str(id))


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
            shutil.move(str(id) + "_merged.jpg", "Z:\\Downloads\\hp\\" + str(id) + ".jpg")

def fetchSlices(id):

    #options = Options()
    #options.headless = True
    #driver = webdriver.Chrome(chrome_options=options)
    options = webdriver.ChromeOptions()
    options.binary_location = r"C:\Program Files\Google\Chrome\Application\chrome.exe"
    options.add_argument("--silent")
    chrome_driver_binary = r".\chromedriver.exe"
    driver = webdriver.Chrome(chrome_driver_binary, chrome_options=options)
    print(type(driver))
    driver.get("https://myHomepage.com/photo/" + str(id))
    button = driver.find_element_by_id('mainphoto')
    button.click()
    driver.implicitly_wait(tg.waitTime) #seconds
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

main()
