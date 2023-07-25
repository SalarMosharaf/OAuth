<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use App\Http\Controllers\ApiController;


class AuthController extends ApiController
{
    function register(Request $request) {

        $validator = Validator::make($request->all(), [
            'name' => 'required|string',
            'family' => 'required|string',
            'email' => 'required|email|unique:users,email',
            'password' => 'required|string',
            'role' => 'required|string',
        ]);

        if ($validator->fails()) {
            return $this->errorResponse($validator->messages(), 422);
        }

        $user = User::create([
            'name' => $request->name,
            'family' => $request->family,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'role' => $request->role
        ]);

        $token = $user->createToken('myApp')->accessToken;


        return $this->successResponse([
            'user' => $user,
            'token' => $token
        ], 201);

    }


    function login(Request $request) {
        
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'password' => 'required|string'
        ]);

        if ($validator->fails()) {
            return $this->errorResponse($validator->messages(), 422);
        }

        $user = User::where('email' , $request->email)->first();

        if(!$user){
            return $this->errorResponse('user not found', 401);
        }
        if(!Hash::check($request->password , $user->password)){
            return $this->errorResponse('password is incorrect', 401);
        }

        $token = $user->createToken('myApp')->accessToken;

        return $this->successResponse([
            'user' => $user,
            'token' => $token
        ], 200);

    }


    function update(Request $request, $email)
    {
        $validator = Validator::make($request->all(), [
            'name' => 'required|string',
            'family' => 'required|string',
            'email' => 'required|email',
        ]);

        if ($validator->fails()) {
            return $this->errorResponse($validator->messages(), 422);
        }

        

        $user = User::where('email',$email)->update([
            'name' => $request->name,
            'family' => $request->family,
            'email' => $request->email
        ]);

        return $this->successResponse($user, 200);
    }

    function get_user(Request $request) {

        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
        ]);

        if ($validator->fails()) {
            return $this->errorResponse($validator->messages(), 422);
        }


        $user = User::where('email' , $request->email)->first();

        if(!$user){
            return $this->errorResponse('user not found', 302);

        }else{

            return $this->successResponse([
                'user' => $user
            ], 200);


        }
        
    }

    
}
