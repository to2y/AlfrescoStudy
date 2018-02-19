<template>
  <div>
    <router-link to="/">Back</router-link>
    <h2>Type Creation</h2>
    <b-container fluid>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Type Name</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="name"
                        id="name"
                        type="text"
                        placeholder="Enter type name" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Title</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="title"
                        id="title"
                        type="text"
                        placeholder="Enter title" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Type Description</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="description"
                        id="description"
                        type="text"
                        placeholder="Enter description" />
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="4" offset-sm="2"><label for="name">Parent type</label></b-col>
        <b-col sm="4">
          <b-form-input v-model="parentType"
                        id="parentType"
                        type="text"
                        placeholder="" />
        </b-col>
      </b-row>
      <b-row>
        <b-col offset-sm="8">
          <b-button-group>
            <router-link :to="{path: '/types/' + modelName}" tag='b-button'>Back</router-link>
            <b-button v-on:click='createNewType'>Create Type</b-button>
          </b-button-group>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import Alf from '@/alfresco/alfresco.js'

export default {
  name: 'TypeCreation',
  props: ['modelName'],
  data () {
    return {
      name: '',
      title: '',
      description: '',
      parentType: 'cm:content'
    }
  },
  created () {
    console.log('here')
  },
  methods: {
    createNewType () {
      Alf.core.customModelApi.createCustomType(this.modelName, this.name, this.parentType, this.title, this.description).then(data => {
        console.log('API called successfully. Returned data: ' + data)
      }, error => {
        console.error(error)
      })
    }
  }
}
</script>
