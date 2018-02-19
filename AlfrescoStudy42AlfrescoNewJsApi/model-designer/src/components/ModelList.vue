<template>
  <div>
    <h2>Custom content model list</h2>
    <div class="action-header">
      <router-link to='/create' tag='b-button'>Create Model</router-link>
    </div>
    <b-table show-empty striped
           :items="modelList.entries" :fields="fields">
           <template slot="name" slot-scope="data">
             <router-link :to="{path: '/types/' +data.item.entry.name}">
               {{data.item.entry.namespacePrefix}}:{{data.item.entry.name}}
             </router-link>
           </template>
           <template slot="status" slot-scope="data">
             {{data.item.entry.status}}
           </template>
           <template slot="Author" slot-scope="data">
             {{data.item.entry.author}}
           </template>
           <template slot="Actions" slot-scope="data">
             <b-button-group size="sm">
               <b-button size="sm" @click.stop="updateModel()" >Update</b-button>
               <b-button size="sm" v-on:click='deleteModel(data.item.entry.name)'>Delete</b-button>
               <b-button size="sm" v-on:click='activateModel(data.item.entry.name)'>Activate</b-button>
               <b-button size="sm" v-on:click='deactivateModel(data.item.entry.name)'>Deactivate</b-button>
             </b-button-group>
           </template>
    </b-table>
  </div>
</template>

<script>
import Alf from '@/alfresco/alfresco.js'

export default {
  name: 'ModelList',
  data () {
    return {
      modelList: [],
      fields: ['name', 'status', 'Author', 'Actions']
    }
  },
  // props: ['alf'],
  created () {
    this.getModels()
  },
  methods: {
    getModels (event) {
      Alf.core.customModelApi.getAllCustomModel().then(data => {
        this.modelList = data.list
      },
      error => {
        console.error(error)
      })
    },
    createNewModel () {
      let status = 'DRAFT'
      let description = 'Test model description'
      let name = 'testModel'
      let namespaceUri = 'http://www.alfresco.org/model/testNamespace/1.0'
      let namespacePrefix = 'test'
      Alf.core.customModelApi.createCustomModel(status, description, name, namespaceUri, namespacePrefix).then(data => {
        console.log('API called successfully. Returned data: ' + data)
        this.getModels()
      }, error => {
        console.error(error)
      })
    },
    updateMode (modelName) {

    },
    deleteModel (modelName) {
      Alf.core.customModelApi.deleteCustomModel(modelName).then(
        data => {
          console.log('Successfully deleted.')
          this.getModels()
        },
        error => {
          console.error(error)
        }
      )
    },
    activateModel (modelName) {
      Alf.core.customModelApi.activateCustomModel(modelName).then(
        data => {
          console.log('Successfully activated.')
          this.getModels()
        },
        error => {
          console.error(error)
        }
      )
    },
    deactivateModel (modelName) {
      Alf.core.customModelApi.deactivateCustomModel(modelName).then(
        data => {
          console.log('Successfully activated.')
          this.getModels()
        },
        error => {
          console.error(error)
        }
      )
    }
  }
}
</script>

<style>
.action-header {
  text-align:left;
  margin-bottom: 4px;
}
</style>
