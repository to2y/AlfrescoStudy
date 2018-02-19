<template>
  <div>
    <h2>Model Creation</h2>
    <b-container fluid>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Model Name</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="name"
                        id="name"
                        type="text"
                        placeholder="Enter model name" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Model Description</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="description"
                        id="description"
                        type="text"
                        placeholder="Enter description" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Model namespace uri</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="namespaceUri"
                        id="namespaceUri"
                        type="text"
                        placeholder="Enter model namespace uri" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Model namespace prefix</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="namespacePrefix"
                        id="namespacePrefix"
                        type="text"
                        placeholder="Enter model namespace prefix" />
        </b-col>
      </b-row>
      <b-row>
        <b-col offset-sm="8">
          <b-button-group>
            <router-link to='/' tag='b-button'>Back</router-link>
            <b-button v-on:click='createNewModel'>Create Model</b-button>
          </b-button-group>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import Alf from '@/alfresco/alfresco.js'

export default {
  name: 'ModelCreation',
  data () {
    return {
      name: '',
      description: '',
      namespaceUri: 'http://www.alfresco.org/study/1.0',
      namespacePrefix: ''
    }
  },
  methods: {
    createNewModel () {
      Alf.core.customModelApi.createCustomModel('DRAFT', this.description, this.name, this.namespaceUri, this.namespacePrefix).then(data => {
        console.log('API called successfully. Returned data: ' + data)
      }, error => {
        console.error(error)
      })
    }
  }
}
</script>
