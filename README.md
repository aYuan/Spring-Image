# Spring-Image [![Build Status](https://travis-ci.org/c1phr/Spring-Image.svg?branch=master)](https://travis-ci.org/c1phr/Spring-Image)

Spring-Image is a library built to make image upload, storage and retrieval simpler in Spring web applications.
Uploads are made simpler by providing data types that can be used in Spring Documents to store MultipartFiles sent by the browser.
These files can then be stored in a data store *as-is* or stored pre-sized to make for quicker retrieval by clients later.

A demo of the library in use can be found [here](https://github.com/c1phr/spring-image-slides/tree/master/demo).

| Supported Formats | Supported Extensions |
| ---------------------- | --- |
| JPEG | .jpg, .jpeg |
| PNG | .png |
| BMP | .bmp, .bm |
| TIFF | .tiff, .tif |
| GIF | .gif |

## Basic Usage

**Option 1 - Include JAR**

The simplest way to leverage Spring-Image is to just download and include the .jar file. You can get the latest release from the [releases page](https://github.com/c1phr/Spring-Image/releases).
Then just include the .jar in your Maven pom:

    <dependency>
        <groupId>com.github.c1phr</groupId>
        <artifactId>spring-image</artifactId>
        <version>0.5</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/spring-image.jar</systemPath>
    </dependency>

**Option 2 - Include Source**

If you're already building a Spring web application want more control over the Spring-Image API, you can include the source right in your project.
Add the package to `src/main/kotlin` and include the Kotlin stdlib in your Maven pom:

    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>1.0.3</version>
    </dependency>

### Data Class Setup
First add an `ImageDataField` type property on the object that you want to add an image to.
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Optional 
    private ImageDataField image;
    
I suggest adding the `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` annotation to prevent sending the full image bytes in JSON to clients.

### Adding Images

The simplest interface for adding images just takes in a MultipartFile object from as it's uploaded from a client. This will store the image in its original size and format.

    public void setImage(MultipartFile uploadedImage) {   
            this.image.addItemImage(uploadedImage);
        }

Images can be pre-optimized at storage time into smaller sizes that better represent your UI needs. If you are only ever going to render profile pictures in 200x200 thumbnails, Spring-Image will let you store the scaled down image so that it can be sent to clients quicker in a named size class.

    public void setImage(MultipartFile uploadedImage) {
        ImageSize size = new ImageSize("sm", 200, 200);        
        this.image.addItemImage(uploadedImage, size);
    }
    
This will allow a smaller version of the image to be retrieved using the "sm" size class.

To store multiple different sized images, pass a `List<ImageSize>` when adding images.

    public void setImage(MultipartFile uploadedImage) {
        ArrayList<ImageSize> sizes = new ArrayList<>();
        sizes.add(new ImageSize("sm", 800, 600));
        sizes.add(new ImageSize("lg", 1600, 1200));
        sizes.add(new ImageSize("hd", 1920, 1080));    
        this.addItemImage(uploadedImage, sizes);
    }
    
By default, Spring-Image will store the original sized image under the `original` size class. This functionality can be disabled by passing `false` as the last parameter.

    this.addItemImage(uploadedImage, sizes, false); // Skip storing the orignal image
 
### Retrieving Images

Spring-Image `ImageDataField` allows you to retrieve `ImageRecord` objects for use in your code, or helper methods for returning images directly through HTTP calls.

`ImageRecord` objects can be retrieved directly from `ImageDataField` by either size key or dimensions. An `ImageRecord` will give access to the bytes storing the original image.

**Get by size key**: Returns null if the size key is not available, sizeKey defaults to "original" if not passed.
    
    ImageRecord img = imageDataField.getImageBySize(sizeKey);
    
**Get by dimensions**: Resizes the image on demand using either the original or otherwise largest available image as the source. Note this can be slow and additional strain on the server depending on the number of on demand resize requests.

    ImageRecord img = imageDataField.getImageBySize(1920, 1080);

**Get by Long/Short Edge**: Very similar to getting an image by dimensions, except scaling happens against just one dimension.

    ImageRecord imgLongEdge = imageDataField.getImageByLongEdge(1920);
    ImageRecord imgShortEdge = imageDataField.getImageByShortEdge(1080);
    
### Retrieval for HTTP Calls
The `HttpImageHandler` class provides an implementation of the retrieval methods described above that are all specifically tailored to writing images into `HttpServletResponse` objects for use in Spring applications. None of the HTTP helpers will return data, they all write image data directly into the response. Note that the `response` variable in these examples is the request's `HttpServletResponse`.

The simplest method returns the "original" size key item if it's stored.

    HttpImageHandler.getSize(response);
    
A different size key can also be passed:

    HttpImageHandler.getSize("lg", response);
    
Getting by dimensions:

    HttpImageHandler.getSize(500, 500, response);
    
Long and short edge:

    HttpImageHandler.getSizeLongEdge(500, response);
    HttpImageHandler.getSizeShortEdge(500, response);
    
## Component Usage
Spring-Image the following components can be used outside of the context of an `ImageDataField`.

### Scaler Component
Scaler can be found at `com.springimage.utils.Scaler`, and currently supports scaling objects of type `MultipartFile`, `BufferedImage`, and `ImageRecord`. Usage is pretty simple:
    
    Scaler scaler = new Scaler(myImage)   
    myImage = scaler.ScaleImage(500, 500);
    
The `Scaler` also exposes `ScaleImageLongEdge(int)` and `ScaleImageShortEdge(int)` to use instead of `ScaleImage(x, y)`.


### Todo:
* Better test coverage
* File format conversion
* Base64 support
* SVG Support
