# Tasks synchronization and parallelization #


The aim of this lab is to correctly organize computation using multiply processes/threads. 


### Variant 10 ###

Use Java, processes and java.nio.channels.AsynchronousSocketChannel and Future<T>. 
Binary operation - conjunction. 
Also generalized to n functions.

### Types of cancellation ###
1. By special key (Ctrl + C)
2. With periodic prompt. Interval between prompts - 5 sec. Also you can call prompt by special key - Ctrl+C.


**Please remember that all operations with special key works only in terminal**. It won't work in IDE run.

If you want special key to work please run managers from jar-files (out/artifacts/manager_periodic_prompt_cancellation/manager_periodic_prompt.jar or out/artifacts/manager_special_key_cancellation/manager_special_key.jar).

For example open folder manager_periodic_prompt_cancellation in terminal and write command **"java -cp manager_periodic_prompt.jar Main"**.



### Test cases: ###
|x  |F result  |F time  |G result   |G time   |Result  |
|---|---|---|---|---|---|
|0   |1   |1018ms   |1   |3021ms   |1|
|2   |0   |3020ms   |   |   |0|
|3   |   |   |0  |3012ms   |0|
|4   |1   |1020ms   |  |inf   |hangs|
|5   |   |inf   |1  |1016ms   |hangs|

