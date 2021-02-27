# How to test on IFI machines
To get more insight, it is a good idea to test on different machines. I have therefore written a short guide on how to test on IFI machines using `scp` and `ssh`.

## 1. Copy your files to your UiO user
This can be done using secure copy `scp`.

```
scp [-r] local_file_path username@login.ifi.uio.no:~/remote_file_path
```
The brackets indicate an optional argument. Use `-r` if you need to copy directories.

Type the command in your terminal using your UiO username and type your UiO password when prompted.

## 2. Log in to an IFI machine
This can be done using `ssh`:

```
ssh username@login.ifi.uio.no
```
Type the command in your terminal using your UiO username and type your UiO password when prompted.

Find the directory where you placed your files and start testing!

## Tip 1: Get CPU specs of the IFI computer
To get the CPU specifications of the IFI computer you are using, in the terminal type:

```
lscpu
```
Here you can find the sizes of the cache, the CPU model, the number of cores and many other things.

## Tip 2: Computer with 64 logical cores
There is a computer called `margir` at IFI that has 64 logical cores. Note however, that it is most likely slower than your computer. If you would like to run your program on it then:

1. Perform `ssh` as described in step 2. This needs to be done as you need to be on IFI network to access margir.

2. Perform `ssh` again to access margir like this:
  ```
  ssh margir.ifi.uio.no
  ```
  Type UiO password when prompted.

3. Type the command `top` to check that no one else is using the computer at the moment (you will see another username at the top). If someone else is running their program, come back in 15 minutes. This means that you also should not run tests that take longer than about 15 minutes. PS: to stop `top`, simply press `q`.
