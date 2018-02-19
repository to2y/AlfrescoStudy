<template>
  <div>
    <b-container fluid class="login-form">
      <img src='../assets/login-top.png'/>
      <b-row>
        <b-col sm="4"><label for="name">User name</label></b-col>
        <b-col sm="8">
          <b-form-input v-model="user"
                        id="user"
                        type="text" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4"><label for="name">Password</label></b-col>
        <b-col sm="8">
          <b-form-input v-model="password"
                        id="user"
                        type="password" />
        </b-col>
      </b-row>
      <b-button class="login-button" v-on:click='login'>Login</b-button>
    </b-container>
  </div>
</template>

<script>
import Alf from '@/alfresco/alfresco.js'

export default {
  name: 'Login',
  data () {
    return {
      user: '',
      password: ''
    }
  },
  methods: {
    login (event) {
      Alf.login(this.user, this.password).then(data => {
        console.log('API called successfully Login in performed ', data)
        this.$router.push('/')
      },
      error => {
        console.log('Error', error)
      })
    }
  }
}
</script>

<style>
.login-form {
  max-width: 500px;
  box-shadow: 2px 2px 4px gray;
  border-radius: 10px;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
}
.login-form > img {
  margin: 20px;
}

.login-button {
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
