**Libraries**
>Below are the libraries I used for the project.
>- Glide: For image lazy loading
>- Hilt: For my Dependency Injection, so I can focus more on the task
>- Mavericks: Android MVI framework, Helps abstract my UI, activity, viewModel.
>- Epoxy: Allows me to build complex ui easily on recyclerView
>- Navigation: Android arch components for navigation
>- Mockito: Unit testing library
>- Apollo Graphql to connect to my my webservice. endpoint to webservice: https://test-filtering-matches.herokuapp.com/

**Other Information**
>- API documentation: https://gnews.io/docs/v4#introduction, 
>- design: https://www.figma.com/file/LP5HJw23ZYX0haT7xPKGXd/test-app?node-id=0%3A1



**Approach**
>- Overall, I broke down the problems to small pieces called `State`. State simply represents the brain of a screen. `Event` are anything that just happened that State should know about.
>- For lazy loading my image and caching using key, I went with glide. As it built to better handle this as it focuses on smooth scrolling
>- I configured github actions to run both lint and test


**Assumptions**
>- I assumed the user's network would be fine to an extent as i implemented a more subtle error handling + retry
