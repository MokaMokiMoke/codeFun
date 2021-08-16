#! python3
# download_xkcd.py - Downloads every single XKCD comic.

import requests
import os
import bs4

#id = 192771
id = 192548
url = 'https://myHomepage.com/photo/' + str(id) # starting url
os.makedirs('tg', exist_ok=True)  # store comics in ./xkcd

while not url.endswith('photo/1'):
    # Download the page.
    print('Downloading page %s...' % url)
    res = requests.get(url)
    res.raise_for_status()

    soup = bs4.BeautifulSoup(res.text)

    # Find the URL of the comic image.
    comic_elem = soup.select('#mainphoto img')
    if not comic_elem:
        print('Could not find comic image.')
    else:
        comic_url = "https://myHomaepage.com/" + comic_elem[0].get('src')
        # Download the image.
        print('Downloading image %s...' % comic_url)
        res = requests.get(comic_url)
        res.raise_for_status()

        # Save the image to ./xkcd.
        image_file = open(os.path.join('tg', os.path.basename(str(id) + ".jpg")),'wb')
        for chunk in res.iter_content(100000):
            image_file.write(chunk)
        image_file.close()

    # Get the Prev button's url.
    #pre_link = soup.select('a[href="/' + str(192771))[0]
    id = id - 1
    #url = pre_link.get('href')
    url = 'https://myHomepage.com/photo/' + str(id)

print('Done.')
