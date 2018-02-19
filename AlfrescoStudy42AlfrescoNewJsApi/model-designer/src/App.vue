<template>
  <div id="app">
    <h1 class="appTitle">Alfresco Content Model Designer(sample)</h1>
    <router-view/>
  </div>
</template>

<script>

export default {
  components: {
  },
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /*margin-top: 60px;*/
}

.btn {
    margin-right: 2px;
}

.row {
  margin-bottom: 4px;
}

h1.appTitle {
  color: #eeeeee;
  background: #44aa44;
  padding: 4px;
}
</style>
