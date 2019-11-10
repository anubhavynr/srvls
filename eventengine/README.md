# 1. PREREQUISTE : All the zip and jar files should exists in the modules folder of EE
# 2. BELOW STEPS WILL : Copy the zip file PushArtificatsToTeamBucket-Lambda.zip to the modules folder of EE 
# 3. DEPLOY THE CLOUDFORMATION: Using EE or manually

export AWS_ACCESS_KEY_ID=ASIA265Q4PFVOKLAWU4Q
export AWS_SECRET_ACCESS_KEY=2MCgldXz9VLsre/jE0hbyf0+rMSEeEpl1z/hA0v4
export AWS_SESSION_TOKEN=FwoGZXIvYXdzEMD//////////wEaDG1IwL3ztz0Ly+8HhiKNAwAhP8yIPUzhd+0iASOfGaKK1tGLrm6p5P07t9gjW4wnKuBJUpp6YM53AW1Z4RJPy1fJyXYaJgq+NAktu5tuF1N6P97z7WTvWhe7NjExXnZxKdQp6BeESiilQFlv+S1d8F8rf1WW9QQq+438XIekR6jP3pWBIlkyWXx9/U9HX0zutt9Ufs66B+IBWPQVJJfl3uXr84OwPXeTXCodZzj3D0/IPnQhdIXffcoWcxtBERWIkEZZ/Dx68USE3c4wJGInjK+lqfXkUk7V7bXWujN59JHKp+bsTAx6t5sog8gbUQgaXpzHCd7h7UbJNj/fOsob4wldwHMPF8TJOUxb8aBP2qxQiC/kt7YWyWGS2Yc5UTkKqfV5KRKadQvRW/Uj3HGsHm3rU0erEnpLYHjVax40gY0T46m1959ueuYXaa2Kx3GjJKwNDVLyy5Qf+LyTdRE+3dWTlqXc4pXXBkXI1NMZ8g/NT5xUFNb6lhoq4ADpzRQ+dA8zmoU/PU9Xs2nhojBR8fPNbBzEIPhV2XltBq0ojvyc7gUyHxiAobOGdNaOBFYcR+k2MYFWx5ZkX5j/VFZEYktWyFw=
export AWS_DEFAULT_REGION=us-east-1

rm PushArtificatsToTeamBucket-Lambda.zip
zip -r PushArtificatsToTeamBucket-Lambda.zip PushArtificatsToTeamBucket-Lambda.py
aws s3 cp PushArtificatsToTeamBucket-Lambda.zip s3://ee-assets-prod-us-east-1/modules/f10ef3b4f001409c893ee7f9decab4c8/v1/PushArtificatsToTeamBucket-Lambda.zip

